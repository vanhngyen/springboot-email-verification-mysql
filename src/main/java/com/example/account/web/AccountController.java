package com.example.account.web;

import com.example.account.model.VerificationForm;
import com.example.account.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
public class AccountController {
    @Autowired
    VerificationTokenService verificationTokenService;

    @GetMapping("/")
    public String index () {
        return "redirect:/email-verification";
    }

    @GetMapping("/email-verification")
    public String formGet (Model model) {
        model.addAttribute("verification", new VerificationForm());
        return "verification-form";
    }

    @PostMapping("/email-verification")
    public String formPost (@Valid VerificationForm verificationForm, BindingResult bindingResult, Model model) throws UnsupportedEncodingException, MessagingException {
        if ( !bindingResult.hasErrors() ) {
            model.addAttribute("noErrors", true);
            model.addAttribute("verification", verificationForm);
            verificationTokenService.createVerification(verificationForm.getEmail());
        }
        return "verification-form";
    }

    @GetMapping("/verify-email")
    @ResponseBody
    public String verifyEmail(String code){
        return verificationTokenService.verifyEmail(code).getBody();
    }
}
