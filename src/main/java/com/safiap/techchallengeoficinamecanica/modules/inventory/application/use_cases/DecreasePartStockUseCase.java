package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.AlterPartResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DecreasePartStockUseCase {

    private static final Logger log = LoggerFactory.getLogger(DecreasePartStockUseCase.class);
    private static final int LOW_STOCK_THRESHOLD = 5;

    private final PartRepository partRepository;

    public AlterPartResponse execute(UUID partId, int amount) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new NotFoundException("part not found: " + partId));
        part.decreaseStock(amount);

        int remaining = part.getQuantity().value();
        if (remaining == 0) {
            log.warn("Part {} ({}) is OUT OF STOCK", part.getId(), part.getName());
        } else if (remaining <= LOW_STOCK_THRESHOLD) {
            log.warn("Part {} ({}) low stock: {} unit(s) left", part.getId(), part.getName(), remaining);
        }

        return partRepository.save(part)
                .map(p -> new AlterPartResponse(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getQuantity().value(),
                        p.getPrice().amount()
                ))
                .orElseThrow(() -> new ConflictException("Fail to update part stock"));
    }
}
