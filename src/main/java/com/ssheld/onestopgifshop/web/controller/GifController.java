package com.ssheld.onestopgifshop.web.controller;

import com.ssheld.onestopgifshop.model.Gif;
import com.ssheld.onestopgifshop.service.CategoryService;
import com.ssheld.onestopgifshop.service.GifService;
import com.ssheld.onestopgifshop.validator.GifValidator;
import com.ssheld.onestopgifshop.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
@Controller
public class GifController {
    @Autowired
    private GifService gifService;

    @Autowired
    private CategoryService categoryService;

    @InitBinder("gif")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new GifValidator());
    }

    // Home page - index of all GIFs
    @RequestMapping("/")
    public String listGifs(Model model) {
        // Get all gifs
        List<Gif> gifs = gifService.findAll();

        model.addAttribute("gifs", gifs);
        return "gif/index";
    }

    // Single GIF page
    @RequestMapping("/gifs/{gifId}")
    public String gifDetails(@PathVariable Long gifId, Model model) {
        // Get gif whose id is gifId
        Gif gif = gifService.findById(gifId);

        model.addAttribute("gif", gif);
        return "gif/details";
    }

    // GIF image data
    @RequestMapping("/gifs/{gifId}.gif")
    @ResponseBody
    public byte[] gifImage(@PathVariable Long gifId) {
        // Return image data as byte array of the GIF whose id is gifId
        return gifService.findById(gifId).getBytes();
    }

    // Favorites - index of all GIFs marked favorite
    @RequestMapping("/favorites")
    public String favorites(Model model) {
        // Grab all GIFs then check which ones
        // are favorited.
        List<Gif> faves = new ArrayList<>();
        List<Gif> gifs;
        gifs = gifService.findAll();
        for (Gif g : gifs) {
            if (g.isFavorite()) {
                faves.add(g);
            }
        }
        model.addAttribute("gifs",faves);
        model.addAttribute("username","Stephen Sheldon"); // Static username
        return "gif/favorites";
    }

    // Upload a new GIF
    @RequestMapping(value = "/gifs", method = RequestMethod.POST)
    public String addGif(@Valid Gif gif, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // Check for errors
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.gif", bindingResult);
            redirectAttributes.addFlashAttribute("gif", gif);
            return "redirect:/upload";
        }

        // Upload new GIF if data is valid
        gifService.save(gif, gif.getFile());

        // Add flash message for success
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("GIF successfully uploaded!", FlashMessage.Status.SUCCESS));

        // Redirect browser to new GIF's detail view
        return String.format("redirect:/gifs/%s", gif.getId());
    }

    // Form for uploading a new GIF
    @RequestMapping("/upload")
    public String formNewGif(Model model) {
        // Add model attribute if it doesn't already exist in the model
        if (!model.containsAttribute("gif")) {
            model.addAttribute("gif", new Gif());
        }
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("action", "/gifs");
        model.addAttribute("heading", "Upload");
        model.addAttribute("submit", "Add");

        return "gif/form";
    }

    // Form for editing an existing GIF
    @RequestMapping(value = "/gifs/{gifId}/edit")
    public String formEditGif(@PathVariable Long gifId, Model model) {
        // Add model attributes needed for edit form
        if (!model.containsAttribute("gif")) {
            model.addAttribute("gif", gifService.findById(gifId));
        }
        // TODO - Add GIF file as default file selected in "upload file"
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("action", String.format("/gifs/%s",gifId));
        model.addAttribute("heading", "Edit GIF");
        model.addAttribute("submit", "Update");

        return "gif/form";
    }

    // Update an existing GIF
    @RequestMapping(value = "/gifs/{gifId}", method = RequestMethod.POST)
    public String updateGif(@Valid Gif gif, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Include the validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
            // Add gif if invalid data was received
            redirectAttributes.addFlashAttribute("gif", gif);
            // Redirect back to the form
            return String.format("redirect:/gif/%s/edit", gif.getId());
        }

        gifService.save(gif, gif.getFile());

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Gif successfully updated!", FlashMessage.Status.SUCCESS));

        return String.format("redirect:/gifs/%s", gif.getId());
    }

    // Delete an existing GIF
    @RequestMapping(value = "/gifs/{gifId}/delete", method = RequestMethod.POST)
    public String deleteGif(@PathVariable Long gifId, RedirectAttributes redirectAttributes) {
        Gif gif = gifService.findById(gifId);
        // Delete the GIF whose id is gifId
        gifService.delete(gif);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Gif deleted.", FlashMessage.Status.SUCCESS));
        // Redirect to app root
        return "redirect:/";
    }

    // Mark/unmark an existing GIF as a favorite
    @RequestMapping(value = "/gifs/{gifId}/favorite", method = RequestMethod.POST)
    public String toggleFavorite(@PathVariable Long gifId, HttpServletRequest request) {
        // With GIF whose id is gifId, toggle the favorite field
        Gif gif = gifService.findById(gifId);
        gifService.toggleFavorite(gif);
        // Redirect to GIF's detail view
        return String.format("redirect:%s", request.getHeader("referer"));
    }
}