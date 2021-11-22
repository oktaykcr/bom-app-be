package com.oktaykcr.bomappbe.controller;

import com.oktaykcr.bomappbe.common.ApiResponse;
import com.oktaykcr.bomappbe.common.ListResponse;
import com.oktaykcr.bomappbe.model.component.ComponentUsed;
import com.oktaykcr.bomappbe.service.component.ComponentUsedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/componentused")
public class ComponentUsedController {

    private final ComponentUsedService componentUsedService;

    @Autowired
    public ComponentUsedController(ComponentUsedService componentUsedService) {
        this.componentUsedService = componentUsedService;
    }

    @GetMapping(value = "")
    public ListResponse<ComponentUsed> list(@RequestParam(value = "pageNumber") Integer pageNumber,
                                        @RequestParam(value = "pageOffset") Integer pageOffset) {
        return componentUsedService.list(pageNumber, pageOffset);
    }

    @GetMapping(value = "/{id}")
    public ApiResponse<ComponentUsed> findById(@PathVariable("id") String id) {
        return ApiResponse.response(componentUsedService.findById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ApiResponse<Boolean> deleteById(@PathVariable("id") String id) {
        return ApiResponse.response(componentUsedService.deleteById(id));
    }

    @PostMapping(value = "")
    public ApiResponse<ComponentUsed> save(@RequestBody ComponentUsed componentUsed) {
        return ApiResponse.response(componentUsedService.save(componentUsed));
    }

    @PutMapping(value = "")
    public ApiResponse<ComponentUsed> update(@RequestBody ComponentUsed componentUsed) {
        return ApiResponse.response(componentUsedService.update(componentUsed));
    }
}
