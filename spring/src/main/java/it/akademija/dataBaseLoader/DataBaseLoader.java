package it.akademija.dataBaseLoader;

import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.repository.DocumentTypeRepository;
import it.akademija.files.repository.FileRepository;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserGroupEntity;
import it.akademija.users.repository.UserGroupRepository;
import it.akademija.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataBaseLoader implements CommandLineRunner {

    @Autowired
    private final DocumentTypeRepository documentTypeRepository;

    @Autowired
    private final DocumentRepository documentRepository;

    @Autowired
    private final FileRepository fileRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserGroupRepository userGroupRepository;

    public DataBaseLoader(DocumentTypeRepository documentTypeRepository, DocumentRepository documentRepository, FileRepository fileRepository, UserRepository userRepository, UserGroupRepository userGroupRepository) {
        this.documentTypeRepository = documentTypeRepository;
        this.documentRepository = documentRepository;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public void run(String... args) throws Exception {

//        /* DUOMBAZES UZPILDYMAS. ATKOMENTUOT, PALEISTI SPRINGA PER IDEA, UZKOMENTUOT ATGAL */
//
//        UserEntity userJonas=new UserEntity("111", "jonas", "jonaitis", "jonas", "slaptazodis1");
//        UserEntity userPetras=new UserEntity("222", "petras", "petraitis", "petras", "slaptazodis2");
//        UserEntity userVytas=new UserEntity("333", "vytas", "lansbergis", "vytas", "slaptazodis3");
//
//        this.userRepository.save(userJonas);
//        this.userRepository.save(userPetras);
//        this.userRepository.save(userVytas);
//
//        UserGroupEntity userGroupUsers=new UserGroupEntity("users");
//        UserGroupEntity userGroupAdministracija=new UserGroupEntity("administration");
//
//        this.userGroupRepository.save(userGroupUsers);
//        this.userGroupRepository.save(userGroupAdministracija);
//
//        /* DOKUMENTU IR JU TIPU PRIDEJIMAS PRIE GRUPIU */
//
//        DocumentEntity documentAtostogos=new DocumentEntity("atostogos", "prasymas atostogu", "atostogos");
//
//        DocumentTypeEntity documentTypeAtostogos=new DocumentTypeEntity("atostogos");
//
//        this.documentTypeRepository.save(documentTypeAtostogos);
//        this.documentRepository.save(documentAtostogos);
//
//
//        /* GRUPIU PRIDEJIMAS PRIE USERIU */
//
//        Set<UserGroupEntity> grupes1=new HashSet<>();
//        grupes1.add(userGroupUsers);
//
//        Set<UserGroupEntity> grupes2=new HashSet<>();
//        grupes2.add(userGroupAdministracija);
//
//        userJonas.setUserGroups(grupes1);
//        userPetras.setUserGroups(grupes1);
//        userVytas.setUserGroups(grupes2);
//
//       this.userRepository.save(userJonas);
//       this.userRepository.save(userPetras);
//       this.userRepository.save(userVytas);

//        /* Grupiu Listai*/
//
//        Set<DocumentTypeEntity> availableDocumentTypesToApprove = new HashSet<>();
//
//            availableDocumentTypesToApprove.add(documentTypeAtostogos);
//
//        Set<DocumentTypeEntity> availableDocumentTypesToUpload = new HashSet<>();
//
//            availableDocumentTypesToUpload.add(documentTypeAtostogos);
//
//        Set<DocumentEntity> documentsToApprove= new HashSet<>();
//
//            documentsToApprove.add(documentAtostogos);
//
//        /* Priskirimas dokumentu tipu ir dokumetu prie grupiu */
//
//        userGroupUsers.setAvailableDocumentTypesToUpload(availableDocumentTypesToUpload);
//
//        userGroupAdministracija.setAvailableDocumentTypesToUpload(availableDocumentTypesToUpload);
//
//        userGroupAdministracija.setAvailableDocumentTypesToApprove(availableDocumentTypesToApprove);
//
//
//        /* Dokumentu idejimas i userio Seta */
//
//        Set<DocumentEntity> documentsInUserList= new HashSet<>();
//
//        documentsInUserList.add(documentAtostogos);
//
//        userJonas.setDocumentEntities(documentsInUserList);


    }
}
