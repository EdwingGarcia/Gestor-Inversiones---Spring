package com.edwinggarcia.Inversiones.controller;

import com.edwinggarcia.Inversiones.model.Operativa;
import com.edwinggarcia.Inversiones.service.OperativaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/operativa")
public class OperativaController {
    @Autowired
    private OperativaService operativaService;

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("operativa", new Operativa());
        return "registro-operativa";
    }

    @PostMapping("/guardar")
    public String guardarOperativa(@ModelAttribute("operativa") Operativa operativa, BindingResult result, Model model) {
        if (result.hasErrors()) {

            return "registro-operativa";
        }
        operativaService.guardar(operativa);

        return "redirect:/inversiones/listar";
    }


}
