package com.ssheld.onestopgifshop.web.controller;


import com.ssheld.onestopgifshop.model.Category;
import com.ssheld.onestopgifshop.service.CategoryServiceImpl;
import com.ssheld.onestopgifshop.web.Color;
import com.ssheld.onestopgifshop.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
/**
 * Author: Stephen Sheldon
 **/

@Controller
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    // Index of all categories
    @SuppressWarnings("unchecked")
    @RequestMapping("/categories")
    public String listCategories(Model model) {
        // Get list of all categories
        List<Category> categories = categoryService.findAll();
        // Add list of categories to our model
        model.addAttribute("categories", categories);
        return "category/index";
    }

    // Single category page
    @RequestMapping("/categories/{categoryId}")
    public String category(@PathVariable Long categoryId, Model model) {

        Category category = categoryService.findById(categoryId);
        model.addAttribute("category", category);
        return "category/details";
    }

    // Form for adding a new category
    @RequestMapping("categories/add")
    public String formNewCategory(Model model) {
        // Add model attributes needed for new form
        if (!model.containsAttribute("category")) {
            model.addAttribute("category", new Category());
        }
        // Else user has submitted invalid data and we want to show the invalid category user submitted
        model.addAttribute("colors", Color.values());
        model.addAttribute("action", "/categories");
        model.addAttribute("heading", "New Category");
        model.addAttribute("submit", "Add");
        return "category/form";
    }

    // Form for editing an existing category
    @RequestMapping("categories/{categoryId}/edit")
    public String formEditCategory(@PathVariable Long categoryId, Model model) {
        // Add model attributes needed for edit form
        if (!model.containsAttribute("category")) {
            model.addAttribute("category", categoryService.findById(categoryId));
        }
        // Else user has submitted invalid data and we want to show the invalid category user submitted
        model.addAttribute("colors", Color.values());
        model.addAttribute("action", String.format("/categories/%s", categoryId));
        model.addAttribute("heading", "Edit Category");
        model.addAttribute("submit", "Update");
        return "category/form";
    }

    // Update an existing category
    @RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.POST)
    public String updateCategory(@Valid Category category, BindingResult result, RedirectAttributes redirectAttributes) {
        // Update category if valid data was received
        if (result.hasErrors()) {
            // Include the validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
            // Add category if invalid data was received
            redirectAttributes.addFlashAttribute("category", category);
            // Redirect back to the form
            return String.format("redirect:/categories/%s/edit", category.getId());
        }
        // Pass in the category to our service save method
        categoryService.save(category);
        // Add flash message
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Category successfully updated!", FlashMessage.Status.SUCCESS));
        // Redirect browser to /categories
        // When Spring sees a response starting with "redirect" it will send a redirect
        // response code along with a location header, which in this case is "/categories"
        return "redirect:/categories";
    }

    // Add a category
    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public String addCategory(@Valid Category category, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            // Include the validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
            // Add category if invalid data was received
            redirectAttributes.addFlashAttribute("category", category);
            // Redirect back to the form
            return "redirect:/categories/add";
        }
        // Pass in the category to our service save method
        categoryService.save(category);
        // Add flash message
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Category successfully added!", FlashMessage.Status.SUCCESS));
        // Redirect browser to /categories
        // When Spring sees a response starting with "redirect" it will send a redirect
        // response code along with a location header, which in this case is "/categories"
        return "redirect:/categories";
    }

    // Delete an existing category
    @RequestMapping(value = "/categories/{categoryId}/delete", method = RequestMethod.POST)
    public String deleteCategory(@PathVariable Long categoryId, RedirectAttributes redirectAttributes) {
        Category cat = categoryService.findById(categoryId);

        // IF category contains GIFS DO NOT delete it!
        if(cat.getGifs().size() > 0) {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Only empty categories can be deleted.", FlashMessage.Status.FAILURE));
            return String.format("redirect:/categories/%s/edit", categoryId);
        }
        // Delete category if it contains no GIFS
        categoryService.delete(cat);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Category deleted!", FlashMessage.Status.SUCCESS));

        return "redirect:/categories";
    }
}

