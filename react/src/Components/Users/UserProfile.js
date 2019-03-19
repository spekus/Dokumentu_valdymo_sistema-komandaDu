import React from 'react';
import DownloadZip from "../FilesAndDocumens/DownloadZip";
// import '../Styles/DownloadZip.css'
// import '../Styles/UserProfile.css'
import '../../App.css'

const UserProfile = (props) => {

    return (
        <div className="container">
            {/*<h2 className="textUP">Mano profilis:</h2>*/}
            <div className="p-3 mb-5 bg-white mainelement borderMain">
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
                            <th>Sukurtų dokumentų archyvas:</th>
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
