package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.mappers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.*;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceOrderMapper {

    public static JPAServiceOrderEntity toJPA(ServiceOrder so) {
        return new JPAServiceOrderEntity(
                so.getServiceOrderId(),
                so.getCustomerId(),
                so.getVehicleId(),
                so.getProblemDescription(),
                so.getStatus(),
                so.getOpenedAt(),
                so.getConcludedAt(),
                so.getBudget() != null ? budgetToJPA(so.getBudget()) : null,
                so.getDiagnosis() != null ? diagnosisToJPA(so.getDiagnosis()) : null,
                so.getAuthorization() != null ? authorizationToJPA(so.getAuthorization()) : null,
                so.getExecutedServices().stream().map(ServiceOrderMapper::executedServiceToJPA).collect(Collectors.toList())
        );
    }

    public static ServiceOrder toDomain(JPAServiceOrderEntity entity) {
        return ServiceOrder.build(
                entity.getId(),
                entity.getCustomerId(),
                entity.getVehicleId(),
                entity.getProblemDescription(),
                entity.getStatus(),
                entity.getOpenedAt(),
                entity.getConcludedAt(),
                entity.getBudget() != null ? budgetToDomain(entity.getBudget()) : null,
                entity.getDiagnosis() != null ? diagnosisToDomain(entity.getDiagnosis()) : null,
                entity.getAuthorization() != null ? authorizationToDomain(entity.getAuthorization()) : null,
                entity.getExecutedServices().stream().map(ServiceOrderMapper::executedServiceToDomain).collect(Collectors.toList())
        );
    }

    private static JPABudgetEntity budgetToJPA(Budget budget) {
        List<JPABudgetItemPartEntity> parts = budget.getPartItems().stream()
                .map(i -> new JPABudgetItemPartEntity(i.getBudgetItemPartId(), i.getPartId(), i.getDescription(), i.getQuantity(), i.getUnitPrice()))
                .collect(Collectors.toList());
        List<JPABudgetItemServiceEntity> services = budget.getServiceItems().stream()
                .map(i -> new JPABudgetItemServiceEntity(i.getBudgetItemServiceId(), i.getDescription(), i.getPrice()))
                .collect(Collectors.toList());
        return new JPABudgetEntity(budget.getBudgetId(), budget.getStatus(), parts, services);
    }

    private static Budget budgetToDomain(JPABudgetEntity entity) {
        List<BudgetItemPart> parts = entity.getPartItems().stream()
                .map(i -> BudgetItemPart.build(i.getId(), i.getPartId(), i.getDescription(), i.getQuantity(), i.getUnitPrice()))
                .collect(Collectors.toList());
        List<BudgetItemService> services = entity.getServiceItems().stream()
                .map(i -> BudgetItemService.build(i.getId(), i.getDescription(), i.getPrice()))
                .collect(Collectors.toList());
        return Budget.build(entity.getId(), entity.getStatus(), parts, services);
    }

    private static JPADiagnosisEntity diagnosisToJPA(Diagnosis diagnosis) {
        return new JPADiagnosisEntity(diagnosis.getDiagnosisId(), diagnosis.getNotes(), diagnosis.getStartedAt(), diagnosis.getConcludedAt());
    }

    private static Diagnosis diagnosisToDomain(JPADiagnosisEntity entity) {
        return Diagnosis.build(entity.getId(), entity.getNotes(), entity.getStartedAt(), entity.getConcludedAt());
    }

    private static JPAAuthorizationEntity authorizationToJPA(Authorization authorization) {
        return new JPAAuthorizationEntity(authorization.getAuthorizationId(), authorization.getNotes(), authorization.getAuthorizedAt());
    }

    private static Authorization authorizationToDomain(JPAAuthorizationEntity entity) {
        return Authorization.build(entity.getId(), entity.getNotes(), entity.getAuthorizedAt());
    }

    private static JPAExecutedServiceEntity executedServiceToJPA(ExecutedService es) {
        return new JPAExecutedServiceEntity(es.getExecutedServiceId(), es.getDescription(), es.getExecutedAt());
    }

    private static ExecutedService executedServiceToDomain(JPAExecutedServiceEntity entity) {
        return ExecutedService.build(entity.getId(), entity.getDescription(), entity.getExecutedAt());
    }
}
