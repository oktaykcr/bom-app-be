package com.oktaykcr.bomappbe.controller;

import com.oktaykcr.bomappbe.common.ApiResponse;
import com.oktaykcr.bomappbe.common.ListResponse;
import com.oktaykcr.bomappbe.model.bom.Bom;
import com.oktaykcr.bomappbe.service.bom.BomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bom")
public class BomController {

    private final BomService bomService;

    @Autowired
    public BomController(BomService bomService) {
        this.bomService = bomService;
    }

    @GetMapping(value = "")
    public ListResponse<Bom> list(@RequestParam(value = "pageNumber") Integer pageNumber,
                                  @RequestParam(value = "pageOffset") Integer pageOffset) {
        return bomService.listPaginated(pageNumber, pageOffset);
    }

    @GetMapping(value = "/search")
    public ListResponse<Bom> searchByTitle(@RequestParam(value = "title") String title) {
        return bomService.searchByTitle(title);
    }

    @GetMapping(value = "/{id}")
    public ApiResponse<Bom> findById(@PathVariable("id") String id) {
        return ApiResponse.response(bomService.findById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ApiResponse<Boolean> deleteById(@PathVariable("id") String id) {
        return ApiResponse.response(bomService.deleteById(id));
    }

    @PostMapping(value = "")
    public ApiResponse<Bom> save(@RequestBody Bom bom) {
        return ApiResponse.response(bomService.save(bom));
    }

    @PutMapping(value = "")
    public ApiResponse<Bom> update(@RequestBody Bom bom) {
        return ApiResponse.response(bomService.update(bom));
    }
}
