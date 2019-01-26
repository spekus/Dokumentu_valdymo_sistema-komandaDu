import React, {Component} from 'react';
import Grid from '@material-ui/core/Grid';
import {unstable_Box as Box} from '@material-ui/core/Box';

class Home extends Component {

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
        var sukurti = this.state.created;
        var patvirtinti = this.state.approved;
        var laukiantys = this.state.awaiting;
        var atmesti = this.state.rejected;


        return (
            <div className="container">
                <h3 className="head">DOKUMENTAI</h3>

                <div className="row">
                    <div className="border border-danger rounded col-sm-5 element1">
                        <h5>Sukurti</h5>

                        <table className="table">
                            <tr>
                                <th>Dokumento tipas</th>
                                <th>Autorius</th>
                                <th>Sukurimo data</th>
                            </tr>


                            <tbody>
                            {this.state.created.map(item => (

                                    <tr key={item.id}>
                                        <td>{item.doctype}</td>
                                        <td>{item.author}</td>
                                        <td>{item.date}</td>
                                    </tr>
                                )
                            )}</tbody>
                        </table>


                    </div>

                    <div className="border border-danger rounded col-sm-5 element1">
                        <h5>Laukiantys patvirtinimo</h5>

                            <table className="table">
                            <tr>
                                <th>Dokumento tipas</th>
                                <th>Autorius</th>
                                <th>Sukurimo data</th>
                            </tr>


                            <tbody>
                            {this.state.awaiting.map(item => (

                                    <tr key={item.id}>
                                        <td>{item.doctype}</td>
                                        <td>{item.author}</td>
                                        <td>{item.date}</td>
                                    </tr>
                                )
                            )}</tbody>
                        </table>


                    </div>

                </div>


                <div className="row">

                    <div className="border border-danger rounded col-sm-5 element1">
                        <h5>Patvirtinti</h5>

                               <table className="table">
                            <tr>
                                <th>Dokumento tipas</th>
                                <th>Autorius</th>
                                <th>Sukurimo data</th>
                            </tr>


                            <tbody>
                            {this.state.approved.map(item => (

                                    <tr key={item.id}>
                                        <td>{item.doctype}</td>
                                        <td>{item.author}</td>
                                        <td>{item.date}</td>
                                    </tr>
                                )
                            )}</tbody>
                        </table>

                    </div>

                    <div className="border border-danger rounded col-sm-5 element1">
                        <h5>Atmesti</h5>

                               <table className="table">
                            <tr>
                                <th>Dokumento tipas</th>
                                <th>Autorius</th>
                                <th>Sukurimo data</th>
                            </tr>


                            <tbody>
                            {this.state.rejected.map(item => (

                                    <tr key={item.id}>
                                        <td>{item.doctype}</td>
                                        <td>{item.author}</td>
                                        <td>{item.date}</td>
                                    </tr>
                                )
                            )}</tbody>
                        </table>
                    </div>
                </div>
            </div>
        );
    }
}

export default Home;

