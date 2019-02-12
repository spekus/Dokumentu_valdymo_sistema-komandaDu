import React from 'react';

const UserProfile = (props) => {
    return (
        <div>
            <h1 className="text-center">Mano profilis:</h1>
            <div className="row">
                <div className="col-md-3"></div>
                <div className="col-md-6">
            <table className="table">
                <tr>
                    <th>Vardas</th>
                    <td>{props.user.firstname}</td>
                </tr>
                <tr>
                    <th>Pavardė</th>
                    <td>{props.user.lastname}</td>
                </tr>
                <tr>
                    <th>Naudotojo vardas</th>
                    <td>{props.user.username}</td>
                </tr>
                {/*<tr>*/}
                    {/*<th>UserID</th>*/}
                    {/*<td>{props.user.userIdentifier}</td>*/}
                {/*</tr>*/}
                <tr>
                    <th>Mano grupės</th>
                    <td>
                        <ul>
                            {props.user.userGroups.map(group =>
                                <li>{group.title}</li>
                            )}
                        </ul>
                    </td>
                </tr>
            </table>
                </div>
                <div className="col-md-3"></div>
            </div>
        </div>
    );
};

export default UserProfile;
