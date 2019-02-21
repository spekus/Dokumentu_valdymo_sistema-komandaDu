import React from 'react';
import DownloadZip from "../FilesAndDocumens/DownloadZip"

const UserProfile = (props) => {
    return (
        <div className="container">
            <h2 className="text-center">Mano profilis:</h2>
            <div className="page1 shadow p-3 mb-5 bg-white rounded">
                    <table className="table">
                        <tbody>
                        <tr>
                            <th>Vardas</th>
                            <td>{props.user.firstname}</td>
                        </tr>
                        <tr>
                            <th>Pavardė:</th>
                            <td>{props.user.lastname}</td>
                        </tr>
                        <tr>
                            <th>Naudotojo vardas:</th>
                            <td>{props.user.username}</td>
                        </tr>
                        <tr>
                            <th>Mano grupės:</th>
                            <td>
                                <ul>
                                    {props.user.userGroups.map((group,index) =>
                                        <li key={index}>{group.title}</li>
                                    )}
                                </ul>
                            </td>
                        </tr>
                        <tr>
                            <th>Duomenys apie mane:</th>
                            <td>
                            
                                <DownloadZip/>
                            
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    
            </div>
        </div>
    );
};

export default UserProfile;
