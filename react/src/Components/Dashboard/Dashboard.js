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
                <Grid container spacing={40}>
                    <Grid item xs>
                        <Typography variant="h5" gutterBottom>Sukurti</Typography>
                        <DocumentsListSimple list={this.state.created}/>
                    </Grid>
                    <Grid item xs>
                        <Typography variant="h5" gutterBottom>Laukiantys patvirtinimo</Typography>
                        <DocumentsListSimple list={this.state.awaiting}/>
                    </Grid>
                </Grid>
                <Grid container spacing={40}>
                    <Grid item xs>
                        <Typography variant="h5" gutterBottom>Patvirtinti</Typography>
                        <DocumentsListSimple list={this.state.approved}/>
                    </Grid>
                    <Grid item xs>
                        <Typography variant="h5" gutterBottom>Atmesti</Typography>
                        <DocumentsListSimple list={this.state.rejected}/>
                    </Grid>
                </Grid>
            </React.Fragment>
        );
    }
}

export default Dashboard;