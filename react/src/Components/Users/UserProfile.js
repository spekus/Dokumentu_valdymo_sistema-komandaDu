import React from 'react';

const UserProfile = (props) => {
    return (
        <div>
            <h2 className="text-center">Mano profilis:</h2>
            <div className="row mt-5">
                <div className="col-md-3"></div>
                <div className="col-md-6">
                    <table className="table">
                        <tbody>
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
                        <tr>
                            <th>Mano grupės</th>
                            <td>
                                <ul>
                                    {props.user.userGroups.map((group,index) =>
                                        <li key={index}>{group.title}</li>
                                    )}
                                </ul>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div className="col-md-3"></div>
            </div>
        </div>
    );
};

export default UserProfile;
