package com.oktaykcr.bomappbe.service.inventory;

import com.oktaykcr.bomappbe.common.ListResponse;
import com.oktaykcr.bomappbe.exception.ApiExceptionFactory;
import com.oktaykcr.bomappbe.exception.ApiExceptionType;
import com.oktaykcr.bomappbe.model.inventory.Inventory;
import com.oktaykcr.bomappbe.model.user.User;
import com.oktaykcr.bomappbe.repository.inventory.InventoryRepository;
import com.oktaykcr.bomappbe.repository.user.UserRepository;
import com.oktaykcr.bomappbe.service.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService extends BaseService<Inventory> {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, UserRepository userRepository) {
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
    }

    public Inventory saveInventoryForUser(String username) {
        if(StringUtils.isBlank(username)) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "username");
        }

        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "user");
        }

        Inventory inventory = new Inventory();
        inventory.setUser(user);

        return inventoryRepository.save(inventory);
    }


    @Override
    public Inventory save(Inventory inventory) {
        return null;
    }

    @Override
    public Inventory update(Inventory inventory) {
        return null;
    }

    @Override
    public ListResponse<Inventory> listPaginated(Integer pageNumber, Integer pageOffset) {
        return null;
    }

    @Override
    public Inventory findById(String id) {
        return null;
    }

    @Override
    public Boolean deleteById(String id) {
        return null;
    }
}
