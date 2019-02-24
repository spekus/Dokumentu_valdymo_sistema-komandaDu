import React, {Component} from 'react';
import SettingsDocumentTypes from "./SettingsDocumentTypes";
import SettingsUserGroups from "./SettingsUserGroups";

class Settings extends Component {
    render() {
        return (
            <React.Fragment>
                {this.props.user.isAdmin ?
                    <div className="container">
                        <h3>Dokumentai</h3>
                        <SettingsDocumentTypes/>
                        <h3>Naudotojai</h3>
                        <SettingsUserGroups/>
                    </div>
                    :
                    <div>
                        <h1><i className="fas fa-skull-crossbones text-center"></i></h1>
                    </div>
                }
            </React.Fragment>
        );
    }
}

export default Settings;