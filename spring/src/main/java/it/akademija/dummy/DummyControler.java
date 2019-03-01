package it.akademija.dummy;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.akademija.documents.service.DocumentTypeService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
@RestController
@Api(value="dummy_data")
@RequestMapping(value = "/api/dummy_data")
public class DummyControler {
    @Autowired
    DummyDataService dummyDataService;

    @RequestMapping(value = "/dummy/{title}", method = RequestMethod.POST)
//    @RequestMapping( method = RequestMethod.DELETE)

    @ApiOperation(value="Delete document type", notes="Deletes document type")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public void augisTestuoja (@PathVariable("title")  @NotNull @Length(min=1) String title) {
//    public void deleteDocumentType ( String title) {
        dummyDataService.augisTestuoja(title);
    }
}
