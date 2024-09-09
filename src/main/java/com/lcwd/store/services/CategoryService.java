package com.lcwd.store.services;

import com.lcwd.store.dtos.CategoryDto;
import com.lcwd.store.dtos.PageableResponse;

public interface CategoryService {
    //create
    CategoryDto create(CategoryDto categoryDto);
    
    //update
    CategoryDto update(CategoryDto categoryDto,String categoryId);
    
    //delete
    void delete(String categoryId);
    
    //get all
    PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);
    
   // get single category details
    CategoryDto get(String categoryId);
    //search
    
}
