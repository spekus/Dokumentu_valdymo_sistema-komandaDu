import React from 'react';
import axios from "axios";
import uuid from 'uuid';

const EditUserGroups = (props) => {

    const addUserToGroup = (username, groupTitle) => {
        axios.put('/kodas-spring-1.0-SNAPSHOT/api/usergroups/' + groupTitle + '/add-person', null, {
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
        axios.put('/kodas-spring-1.0-SNAPSHOT/api/usergroups/' + groupTitle + '/remove-person', null, {
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
                    {props.allgroups
                        .filter((group) => group.title !== 'Suspenduoti vartotojai')
                        .map((group, index) =>
                        props.user.userGroups.map(group => group.title).indexOf(group.title) > -1 ?
                            <tr key={uuid()}>
                                <td>{group.title}</td>
                                <td><button className="buttonlink"
                                       onClick={() => removeUserFromGroup(props.user.username, group.title)}>▶</button>
                                </td>
                                <td></td>
                            </tr>
                            :
                            <tr key={uuid()}>
                                <td></td>
                                <td><button className="buttonlink"
                                            onClick={() => addUserToGroup(props.user.username, group.title)}>◀</button>
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
