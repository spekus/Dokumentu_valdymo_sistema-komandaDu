import React from 'react';
import axios from "axios";

const EditUserGroups = (props) => {

    const addUserToGroup = (username, groupTitle) => {
        axios.put('/api/usergroups/' + groupTitle + '/add-person', null, {
            params: {username: username}
        })
            .then(response => {
                props.onGroupsChanged();
            })
            .catch(error => {
                console.log("Error from removeUserFromGroup - " + error)
            })
    }

    const removeUserFromGroup = (username, groupTitle) => {
        axios.put('/api/usergroups/' + groupTitle + '/remove-person', null, {
            params: {
                username: username
            }
        })
            .then(response => {
                props.onGroupsChanged();
            })
            .catch(error => {
                console.log("Error from removeUserFromGroup - " + error)
            })
    }

    return (
        <div>
            <React.Fragment>
                <table className="table">
                    <thead>
                    <tr>
                        <th style={{width: '45%'}}>Priklauso</th>
                        <th>&nbsp;</th>
                        <th style={{width: '45%'}}>Nepriklauso</th>
                    </tr>
                    </thead>
                    <tbody>
                    {props.allgroups.map((group, index) =>
                        props.user.userGroups.map(group => group.title).indexOf(group.title) > -1 ?
                            <tr>
                                <td>{group.title}</td>
                                <td><a href="#"
                                       onClick={() => removeUserFromGroup(props.user.username, group.title)}>▶</a>
                                </td>
                                <td></td>
                            </tr>
                            :
                            <tr>
                                <td></td>
                                <td><a href="#"
                                       onClick={() => addUserToGroup(props.user.username, group.title)}>◀</a>
                                </td>
                                <td>{group.title}</td>
                            </tr>
                    )}
                    </tbody>
                </table>
            </React.Fragment>
        </div>
    );
};

export default EditUserGroups;
