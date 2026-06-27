package com.safiap.techchallengeoficinamecanica.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end integration test of the full service order lifecycle.
 * Request payloads are loaded from src/test/resources/requests.json (mirrors the CURL collection).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ServiceOrderFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private JsonNode fixtures;

    @Test
    void fullServiceOrderLifecycle() throws Exception {
        // 1. Register the admin user
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(body("register"))))
                .andExpect(status().isCreated());

        // 2. Login -> capture JWT
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(body("login"))))
                .andExpect(status().isOk())
                .andReturn();
        String token = field(loginResult, "accessToken");
        assertThat(token).isNotBlank();

        // 3. Register customer -> capture customerId
        MvcResult customerResult = mockMvc.perform(authPost("/customers/register", body("customer"), token))
                .andExpect(status().isCreated())
                .andReturn();
        String customerId = field(customerResult, "customerId");

        // 4. Register vehicle -> capture vehicleId
        Map<String, Object> vehicle = body("vehicle");
        vehicle.put("customerId", customerId);
        MvcResult vehicleResult = mockMvc.perform(authPost("/vehicles/register", vehicle, token))
                .andExpect(status().isCreated())
                .andReturn();
        String vehicleId = field(vehicleResult, "vehicleId");

        // 5. Register a part in inventory -> itemId reused in the budget
        MvcResult partResult = mockMvc.perform(authPost("/part", body("part"), token))
                .andExpect(status().isCreated())
                .andReturn();
        String partId = field(partResult, "id");

        // 6. Register a service in inventory
        MvcResult serviceResult = mockMvc.perform(authPost("/service", body("service"), token))
                .andExpect(status().isCreated())
                .andReturn();
        String serviceId = field(serviceResult, "id");

        // 7. Open the service order (status RECEIVED)
        Map<String, Object> openSo = body("openServiceOrder");
        openSo.put("customerId", customerId);
        openSo.put("vehicleId", vehicleId);
        MvcResult soResult = mockMvc.perform(authPost("/service-orders", openSo, token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("RECEIVED"))
                .andReturn();
        String serviceOrderId = field(soResult, "serviceOrderId");

        // 8. Start diagnosis -> IN_DIAGNOSIS
        mockMvc.perform(authPatch("/service-orders/" + serviceOrderId + "/start-diagnosis", null, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_DIAGNOSIS"));

        // 9. Open budget
        mockMvc.perform(authPost("/service-orders/" + serviceOrderId + "/budget", null, token))
                .andExpect(status().isCreated());

        // 10. Add part to budget
        Map<String, Object> addPart = body("addPart");
        addPart.put("itemId", partId);
        mockMvc.perform(authPost("/service-orders/" + serviceOrderId + "/budget/parts", addPart, token))
                .andExpect(status().isCreated());

        // 11. Add service to budget
        Map<String, Object> addService = body("addService");
        addService.put("itemId", serviceId);
        mockMvc.perform(authPost("/service-orders/" + serviceOrderId + "/budget/services", addService, token))
                .andExpect(status().isCreated());

        // 12. Get budget -> total = 89.90 + 150.00
        MvcResult budgetResult = mockMvc.perform(authGet("/service-orders/" + serviceOrderId + "/budget", token))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode budget = objectMapper.readTree(budgetResult.getResponse().getContentAsString());
        assertThat(budget.get("totalAmount").decimalValue()).isEqualByComparingTo(new BigDecimal("239.90"));

        // 13. Finalize budget
        mockMvc.perform(authPatch("/service-orders/" + serviceOrderId + "/budget/finalize", null, token))
                .andExpect(status().isOk());

        // 14. Finalize diagnosis (sets the diagnosis description) -> AWAITING_APPROVAL
        String expectedDiagnosis = fixtures().get("finalizeDiagnosis").get("diagnosis").asText();
        mockMvc.perform(authPatch("/service-orders/" + serviceOrderId + "/finalize-diagnosis", body("finalizeDiagnosis"), token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AWAITING_APPROVAL"))
                .andExpect(jsonPath("$.diagnosis").value(expectedDiagnosis));

        // 15. Execute (customer approved) -> IN_EXECUTION
        mockMvc.perform(authPatch("/service-orders/" + serviceOrderId + "/execute", null, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_EXECUTION"));

        // 16. Record service time
        mockMvc.perform(authPost("/service-orders/" + serviceOrderId + "/time-records", body("recordServiceTime"), token))
                .andExpect(status().isCreated());

        // 17. Finalize order -> FINALIZED
        mockMvc.perform(authPatch("/service-orders/" + serviceOrderId + "/finalize", null, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINALIZED"));

        // 18. Deliver -> DELIVERED
        mockMvc.perform(authPatch("/service-orders/" + serviceOrderId + "/deliver", null, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    void protectedEndpointWithoutTokenIsRejected() throws Exception {
        mockMvc.perform(get("/service-orders").param("status", "RECEIVED"))
                .andExpect(status().isUnauthorized());
    }

    // ---- helpers ----

    private JsonNode fixtures() throws Exception {
        if (fixtures == null) {
            try (InputStream is = new ClassPathResource("requests.json").getInputStream()) {
                fixtures = objectMapper.readTree(is);
            }
        }
        return fixtures;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> body(String key) throws Exception {
        return objectMapper.convertValue(fixtures().get(key), Map.class);
    }

    private String json(Map<String, Object> map) throws Exception {
        return objectMapper.writeValueAsString(map);
    }

    private String field(MvcResult result, String field) throws Exception {
        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        return node.get(field).asText();
    }

    private MockHttpServletRequestBuilder authPost(String url, Map<String, Object> body, String token) throws Exception {
        MockHttpServletRequestBuilder rb = post(url).header("Authorization", "Bearer " + token);
        if (body != null) {
            rb = rb.contentType(MediaType.APPLICATION_JSON).content(json(body));
        }
        return rb;
    }

    private MockHttpServletRequestBuilder authPatch(String url, Map<String, Object> body, String token) throws Exception {
        MockHttpServletRequestBuilder rb = patch(url).header("Authorization", "Bearer " + token);
        if (body != null) {
            rb = rb.contentType(MediaType.APPLICATION_JSON).content(json(body));
        }
        return rb;
    }

    private MockHttpServletRequestBuilder authGet(String url, String token) {
        return get(url).header("Authorization", "Bearer " + token);
    }
}
