package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    CheeseDao cheeseDao;

    @Autowired
    MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("title", "Menus");
        model.addAttribute("menus", menuDao.findAll());
        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model) {
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMenuForm(Model model, @ModelAttribute @Valid Menu newMenu, Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }
        menuDao.save(newMenu);
        model.addAttribute("menu", newMenu);

        return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value = "view/{id}")
    public String displayViewMenuForm(Model model, @PathVariable int id) {
        Menu men = menuDao.findOne(id);
        model.addAttribute("title", men.getName());
        model.addAttribute("menu", men);
//        model.addAttribute("cheeses",men.)
        return "menu/view";
    }

    @RequestMapping(value = "add-item/{id}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int id) {
        Menu men = menuDao.findOne(id);
        AddMenuItemForm form = new AddMenuItemForm(men, cheeseDao.findAll());
        model.addAttribute("title", "Add item to menu: " + men.getName());
        model.addAttribute("form", form);
        return "menu/add-item";
    }

    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm form, Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add item to menu: " + form.getMenu().getName());
            return "menu/add-item";
        }

        Menu men = menuDao.findOne(form.getMenuId());
        Cheese cheese = cheeseDao.findOne(form.getCheeseId());

//        Test
        int cheeseId = form.getCheeseId();
        int menuId = form.getMenuId();

        men.addItem(cheese);
        menuDao.save(men);

        return "redirect:/menu/view/" + men.getId();
    }
    }
