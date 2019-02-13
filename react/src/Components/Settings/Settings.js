import React, {Component} from 'react';
import SettingsDocumentTypes from "./SettingsDocumentTypes";
import SettingsUserGroups from "./SettingsUserGroups";

class Settings extends Component {
    render() {
        return (
            <React.Fragment>
                {this.props.user.isAdmin ?
                    <div>
                        <h3>Dokumentai</h3>
                        <SettingsDocumentTypes/>
                        <h3>Naudotojai</h3>
                        <SettingsUserGroups/>
                    </div>
                    :
                    <div>
                        <h1>Ne, jums cia negalima</h1>
                    </div>
                }
            </React.Fragment>
        );
    }
}

export default Settings;