import React from 'react';
import axios from "axios";


const SettingsEditGroupTypes = (props) => {

    const handleChangeUpload = (group, type, event) => {
        event.preventDefault();
        if (event.target.checked) {
            addTypeForUploadToGroup(group, type);
        } else {
            removeTypeForUploadfromGroup(group, type);
        }
    }

    const handleChangeApprove = (group, type, event) => {
        event.preventDefault();

        if (event.target.checked) {
            addTypeForApproveToGroup(group, type);
        } else {
            removeTypeForApprovefromGroup(group, type);
        }
    }


    const addTypeForUploadToGroup = (group, type) => {
        console.log("Group for editing - " + group.title);
        let doctype = type.title;
        axios.put('/kodas-spring-1.0-SNAPSHOT/api/usergroups/' + group.title + '/add-document-type-to-upload', null, {params: {documentTypeTitle: doctype}})
            .then(response => {
                props.onChange();
            })
            .catch(error => {
                console.log("Error from addTypeForUploadToGroup" + error.message)
            })
    }

    const addTypeForApproveToGroup = (group, type) => {
        console.log("Group for editing - " + group.title);
        let doctype = type.title;
        // let doctype = window.prompt("Iveskite dokumento tipą, kurį norite pridėti grupei " + group.title);
        axios.put('/kodas-spring-1.0-SNAPSHOT/api/usergroups/' + group.title + '/add-document-type-to-approve', null, {params: {documentTypeTitle: doctype}})
            .then(response => {
                props.onChange();
            })
            .catch(error => {
                console.log("Error from addTypeForApproveToGroup" + error.message)
            })

    }


    const removeTypeForUploadfromGroup = (group, type) => {
        axios.put("/kodas-spring-1.0-SNAPSHOT/api/usergroups/" + group.title + "/remove-document-type-to-upload", null, {params: {documentTypeTitle: type.title}})
            .then(response => {
                props.onChange();
                console.log(group.title + " group removed")
            })
            .catch(error => {
                console.log("Error from removeTypefromGroup" + error.message)
            })
    }


    const removeTypeForApprovefromGroup = (group, type) => {
        axios.put("/kodas-spring-1.0-SNAPSHOT/api/usergroups/" + group.title + "/remove-document-type-to-approve", null, {params: {documentTypeTitle: type.title}})
            .then(response => {
                props.onChange();
                console.log(group.title + " group removed")
            })
            .catch(error => {
                console.log("Error from removeTypefromGroup" + error.message)
            })
    }


    return (
        <div>
            <h5>Grupė {props.group.title}</h5>
            <table className="table">
                <thead>
                <tr>
                    <th>Tipas</th>
                    <th>Gali kurti</th>
                    <th>Gali tvirtinti</th>
                </tr>
                </thead>
                <tbody>

                {props.alltypes.map((type, index) =>
                    <tr key={index}>
                        <td>{type.title}</td>
                        <td>
                            <input
                                type="checkbox"
                                checked={props.group.typesToUpload.map(t => t.title).indexOf(type.title)
                                > -1}
                                onChange={(event) => handleChangeUpload(props.group, type, event)}/>
                        </td>
                        <td>
                            <input
                                type="checkbox"
                                checked={props.group.typesToApprove.map(t => t.title).indexOf(type.title)
                                > -1}
                                onChange={(event) => handleChangeApprove(props.group, type, event)}/>
                        </td>
                    </tr>
                )}

                </tbody>
            </table>

        </div>

    );

}

export default SettingsEditGroupTypes;