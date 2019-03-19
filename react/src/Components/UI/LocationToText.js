import React from 'react';

const LocationToText = (props) => {
    let name = props.location.pathname;

    switch (props.location.pathname) {
        case '/profile':
            name = "Naudotojo profilis";
            break;
        case '/dashboard/documents/all':
            name = "Visi dokumentai";
            break;

        case '/dashboard/documents/all/':
            name = "Visi dokumentai";
            break;

        case '/dashboard/documents/created/':
            name = "Sukurti dokumentai";
            break;

        case '/dashboard/documents/submitted/':
            name = "Pateikti dokumentai";
            break;

        case '/dashboard/documents/approved/':
            name = "Patvirtinti dokumentai";
            break;

        case '/dashboard/documents/rejected/':
            name = "Atmesti dokumentai";
            break;

        case '/dashboard/documents/rejected/':
            name = "Atmesti dokumentai";
            break;

        case '/dashboard/documents/to_aproove/':
            name = "Dokumentai tvirtinimui";
            break;

        case '/upload-file':
            name = "Naujo dokumento kūrimas";
            break;
        case '/user-administration-list':
            name = "Naudotojų administravimas";
            break;
        case '/settings':
            name = "Nustatymai";
            break;
        case '/statistics':
            name = "Statistika";
            break;
        case '/auditlog':
            name = "Veiksmų žurnalas";
            break;
        default:
            return name = ""
    }

    return (
        <span>
            {name}
        </span>
    );
};

export default LocationToText;
