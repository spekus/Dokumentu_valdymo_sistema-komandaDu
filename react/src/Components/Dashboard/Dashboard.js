import React, {Component} from 'react';
import Typography from "@material-ui/core/Typography/Typography";
import Grid from "@material-ui/core/Grid/Grid";
import DocumentsListSimple from "../Documents/DocumentsListSimple";

class Dashboard extends Component {
    state = {
        created: [
            {id: 1, doctype: "Atostogų prašymas", author: "VardasPavarde1", date: "2019.01.01"},
            {id: 2, doctype: "Bendri dokumentai", author: "VardasPavarde2", date: "2019.01.02"},
            {id: 3, doctype: "Projektas1", author: "VardasPavarde3", date: "2019.01.03"}
        ],
        approved: [
            {id: 4, doctype: "Atostogų prašymas", author: "VardasPavarde4", date: "2019.01.01"},
            {id: 5, doctype: "Bendri dokumentai", author: "VardasPavarde5", date: "2019.01.02"},
            {id: 6, doctype: "Projektas2", author: "VardasPavarde3", date: "2019.01.03"}
        ],
        rejected: [
            {id: 7, doctype: "Atostogų prašymas", author: "VardasPavarde1", date: "2019.01.01"},

        ],
        awaiting: [
            {id: 8, doctype: "Atostogų prašymas", author: "VardasPavarde1", date: "2019.01.01"},
            {id: 9, doctype: "Bendri dokumentai", author: "VardasPavarde2", date: "2019.01.02"},
        ]
    }


    render() {
        return (
            <React.Fragment>
                <div className="row">
                    <div className='col-lg-6'>
                        <h5>Sukurti</h5>
                        <DocumentsListSimple list={this.state.created}/>
                    </div>
                    <div className='col-lg-6'>
                        <h5>Laukiantys patvirtinimo</h5>
                        <DocumentsListSimple list={this.state.awaiting}/>
                    </div>
                </div>
                <div className="row mt-2">
                    <div className='col-lg-6'>
                        <h5>Patvirtinti</h5>
                        <DocumentsListSimple list={this.state.approved}/>
                    </div>
                    <div className='col-lg-6'>
                        <h5>Atmesti</h5>
                        <DocumentsListSimple list={this.state.rejected}/>
                    </div>
                </div>
            </React.Fragment>
        );
    }
}

export default Dashboard;