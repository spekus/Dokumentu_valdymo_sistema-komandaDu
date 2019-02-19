package it.akademija.documents.controller;

import com.zetcode.bean.City;
import com.zetcode.service.ICityService;
import com.zetcode.util.WriteCsvToResponse;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import it.akademija.documents.repository.DocumentEntity;
import it.akademija.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class MyCsvController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/documents", produces = "text/csv")
    public void findCities(HttpServletResponse response,
                           @ApiIgnore Authentication authentication,) throws IOException {

        List<DocumentEntity> documents = (List<DocumentEntity>) userService.getAllUserDocuments();

        WriteCsvToResponse.writeCities(response.getWriter(), documents);
    }

    @RequestMapping(value = "/cities/{cityId}", produces = "text/csv")
    public void findCity(@PathVariable Long cityId, HttpServletResponse response) throws IOException {

        City city = userService.findById(cityId);
        WriteCsvToResponse.writeCity(response.getWriter(), city);
    }
}