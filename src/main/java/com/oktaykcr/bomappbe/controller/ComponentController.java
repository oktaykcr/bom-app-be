package com.oktaykcr.bomappbe.controller;

import com.oktaykcr.bomappbe.common.ApiResponse;
import com.oktaykcr.bomappbe.common.ListResponse;
import com.oktaykcr.bomappbe.model.component.Component;
import com.oktaykcr.bomappbe.service.component.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/component")
public class ComponentController {

    private final ComponentService componentService;

    @Autowired
    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping(value = "")
    public ListResponse<Component> list(@RequestParam(value = "pageNumber") Integer pageNumber,
                                  @RequestParam(value = "pageOffset") Integer pageOffset) {
        return componentService.list(pageNumber, pageOffset);
    }

    @GetMapping(value = "/{id}")
    public ApiResponse<Component> findById(@PathVariable("id") String id) {
        return ApiResponse.response(componentService.findById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ApiResponse<Boolean> deleteById(@PathVariable("id") String id) {
        return ApiResponse.response(componentService.deleteById(id));
    }

    @PostMapping(value = "")
    public ApiResponse<Component> save(@RequestBody Component component) {
        return ApiResponse.response(componentService.save(component));
    }

    @PutMapping(value = "")
    public ApiResponse<Component> update(@RequestBody Component component) {
        return ApiResponse.response(componentService.update(component));
    }

}
