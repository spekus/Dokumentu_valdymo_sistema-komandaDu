const auditActionTranslations = [
    { value: "CREATE_NEW_USER", translation: "Sukurtas naujas naudotojas"},
    { value: "CREATE_NEW_USERGROUP", translation: "Sukurta nauja naudotojų grupė"},
    { value: "CREATE_NEW_DOCUMENT", translation: "Sukurtas dokumentas"},
    { value: "CREATE_NEW_DOCUMENT_TYPE", translation: "Sukurtas naujas dokumentų tipas"},
    { value: "MODIFY_USER", translation: "Pakeista naudotojo informacija"},
    { value: "MODIFY_USERGROUP", translation: "Pakeista grupė"},
    { value: "MODIFY_DOCUMENT_TYPE", translation: "Pakeistas dokumentų tipo pavadinimas"},
    { value: "DELETE_USERGROUP", translation: "Pašalinta naudotojų grupė"},
    { value: "DELETE_DOCUMENT_TYPE", translation: "Pašalintas dokumentų tipas"},
    { value: "APPROVE_DOCUMENT", translation: "Patvirtintas dokumentas"},
    { value: "REJECT_DOCUMENT", translation: "Atmestas dokumentas"},
    { value: "SUBMIT_DOCUMENT", translation: "Pateiktas dokumentas"},
    { value: "SUSPEND_USER", translation: "Naudotojo teisės užblokuotos"},
    { value: "REMOVE_USER_FROM_GROUP", translation: "Naudotojas pašalintas iš grupės"},
    { value: "ADD_USER_TO_GROUP", translation: "Naudotojas pridėtas prie grupės"},


]

export const translateAction = (action) =>{
    let result =  auditActionTranslations.find(t => t.value === action);

    return result ? result.translation : action;
}