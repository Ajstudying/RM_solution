package com.example.RM_solution.companyService;

import com.example.RM_solution.auth.Auth;
import com.example.RM_solution.auth.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/company")
public class CompanyController {

    @Auth(role = UserRole.COMPANY_MEMBER)
    @PutMapping("/telephone")
    public ResponseEntity editCompanyTelephone() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
