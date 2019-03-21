import React, {Component} from 'react';

import DownloadZip from "../FilesAndDocumens/DownloadZip";
import '../../App.css';
import axios from 'axios';

class UserProfile extends Component {

  state = {
      count: ''
    };

  componentDidMount() {
      this.getNumberOfDocuments();
  }

    getNumberOfDocuments = () => {
    axios.get('/api/documents/count')
        .then(response => {
            this.setState({count: response.data})
        })
  }


    render() {
        return (
            <div className="container">
                <div className="p-3 mb-5 bg-white mainelement borderMain">
                    <table className="table">
                        <tbody>
                        <tr>
                            <th>Vardas</th>
                            <td>{this.props.user.firstname}</td>
                        </tr>
                        <tr>
                            <th>Pavardė:</th>
                            <td>{this.props.user.lastname}</td>
                        </tr>
                        <tr>
                            <th>Naudotojo vardas:</th>
                            <td>{this.props.user.username}</td>
                        </tr>
                        <tr>
                            <th>Mano grupės:</th>
                            <td>
                                <ul>
                                    {this.props.user.userGroups.map((group, index) =>
                                        <li key={index}>{group.title}</li>
                                    )}
                                </ul>
                            </td>
                        </tr>
                        <tr>
                            <th>Sukurtų dokumentų archyvas:</th>
                            <td>
                                {this.state.count > 0?
                                   <DownloadZip/>
                                   :
                                   "Sukurtų dokumentų nėra"
                                }


                            </td>
                        </tr>
                        </tbody>
                    </table>

                </div>
            </div>
        );
    }
}

export default UserProfile;
