package it.akademija.auth;

// Roles turi prasideti butinai su "ROLE_"
// kad mes galetume tikrinti role taip:
// boolean isAdmin = request.isUserInRole("ADMIN");
// SPRING pats priklijuos prefiksa ROLE_ pries tikrinant!
public enum AppRoleEnum {
    ROLE_ADMIN,
    ROLE_USER
}
