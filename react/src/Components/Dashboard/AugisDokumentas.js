import React, {Component} from 'react';

import FileSaver from "file-saver";
import axios from 'axios';

class AugisDokumentas extends Component {
    state = {

        type: '',
        description: '',
        title: '',
        attachedFileIdentifier: '',
        attachedFileName: '',
        userIdentifier: '',
        documentState: "Laukiama patvirtinimo",
        rejectedReason: '',
        documentInfo: {}
    };

    action = this.state.documentInfo.documentState;

    componentWillMount() {
        this.getDocumentInformation();
    }

    componentDidMount() {
    }

    handleChangeInput = (event) => this.setState({[event.target.name]: event.target.value});


    getDocumentInformation = () => {
        let params = new URLSearchParams();
        params.append('documentIdentifier', this.props.match.params.id);
        axios.get('/api/documents', {params: params})
            .then(result => {
                //kelyje turi but uzsifruotas dokumento id
                console.log("Dokumento kelio id - " + this.props.match.params.id);
                console.log("getDocumentInformation" + result)
                // this.setState({availableTypes: result.data});
                // this.setState({type: result.data[0]});
                console.log("Description -" + result.data.description);
                console.log("title -" + result.data.title);
                console.log("type -" + result.data.type);
                // this.setState({
                //     title: result.data.title
                //     , type: result.data.type
                //     , description: result.data.description
                // });
                this.setState({documentInfo: result.data});
                this.getFileList();
            })
            .catch(error => {
                console.log("Atsakymas is getDocumentInformation - " + error)
            })

    }
    getFileList = () => {
        console.log("getFileList is being run")
        axios({
            method: 'GET',
            url: '/api/files/findAllFilesByDocumentIdentifier',
            params: {
                DocumentIdentifier: (this.props.match.params.id)


            },
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        })
            .then(result => {


                this.setState({attachedFileIdentifier: result.data[0].identifier});
                this.setState({attachedFileName: result.data[0].fileName});
                console.log("failo Pavadinimas -  "
                    + result.data[0].fileName)
                console.log("failo identifier "
                    + this.state.attachedFileIdentifier)
                // this.setState({type: result.data[0]});

            })
            .catch(error => {
                console.log("Atsakymas is getFileList - " + error)
            })
    }

    downloadOneFile = (fileIdentifier) => {
        // cia reiketu susitvarkyti su downloaderiu/filesaveriu
        // atsakymas kuris grizta yra dvejetainis, ir jame yra pats failas kuri norime atsisiusti
        // reikia gauti ji kaip BLOB'a (Binary Large OBject) ir issaugoti
        console.log("downloadOneFile start")
        axios.get('/api/files/download/' + fileIdentifier)
            .then(response => {
                // console.log(response);
                // const fileNameHeader = "x-suggested-filename";
                // let suggestedFileName = response.headers[fileNameHeader];
                // let effectiveFileName = (suggestedFileName === undefined
                //     ? "document.txt"
                //     : suggestedFileName);
                // FileSaver.saveAs(response.url, suggestedFileName);
                FileSaver.SaveAs(response.data);
            })
            .catch(error => {
                console.log(error)
            })
    }

    downloadFile = () => {
        // neparasius pilno adreso su localhostu programa atsiuncia nesamone.
        //speju cia kazkas susije su security,
        fetch("http://localhost:8181/api/files/download/" + this.state.attachedFileIdentifier)
            .then(response => {

                console.log(response);
                console.log("download " + this.state.attachedFileIdentifier);
                // Log somewhat to show that the browser actually exposes the custom HTTP header
                const fileNameHeader = "x-suggested-filename";
                const suggestedFileName = response.headers[fileNameHeader];
                const effectiveFileName = (suggestedFileName === undefined
                    ? "document.txt"
                    : suggestedFileName);
                console.log("Received header [" + fileNameHeader + "]: " + suggestedFileName
                    + ", effective fileName: " + effectiveFileName);

                // Let the user save the file.
                FileSaver.saveAs(response.url, suggestedFileName);


            }).catch((response) => {
            console.error("Could not Download the Excel report from the backend.", response);
        });
    }


    submitDocument = (props) => {
        var docID = this.props.match.params.id;
        // var params = new URLSearchParams();
        // params.append('userIdentifier', this.props.user.userIdentifier);
        axios.post("/api/documents/" + docID + "/submit")
            .then(response => {
                // this.getDocumentInformation();
                this.setState({documentState: 'Pateikta'});
            })
            .catch(error => {
                window.alert("Klaida is submitDocument - " + error.message);
            })
    }


    approveDocument = (props) => {
        var docID = this.props.match.params.id;
        // var params = new URLSearchParams();
        // params.append('userIdentifier', this.props.user.userIdentifier);
        axios.post("/api/documents/" + docID + "/approve")
            .then(response => {
                this.setState({documentState: this.state.documentInfo.documentState});
                this.getDocumentInformation();
            })
            .catch(error => {
                window.alert("Klaida is approveDocument - " + error.message)
                // console.log("Klaida is approveDocument - " + error.message);
            })
    }

    rejectDocument = (props) => {
        var reason = window.prompt("Iveskite atmetimo priezasti");
        var docID = this.state.documentInfo.documentIdentifier;
        var params = new URLSearchParams();
        params.append('rejectedReason', reason);
        axios.post("/api/documents/documents/" + docID + "/reject", params)
            .then(response => {
                this.setState({documentState: 'Atmesta'});
            })
            .catch(error => {
                console.log("Klaida is rejectDocument - " + error.message);
            })
    }


    render() {
        return (
            <React.Fragment>
                <div>


                    {/* <h5>Sukurti</h5> */}
                    <table className='table table-bordered col-md-7'>
                        <thead>
                        <th colspan='2' className="text-center table-secondary">DOKUMENTO DETALĖS</th>
                        </thead>
                        <tbody>
                        <tr>
                            <th>Dokumento pavadinimas</th>
                            <td>{this.state.documentInfo.title}</td>
                        </tr>
                        <tr>
                            <th>Dokumento tipas</th>
                            <td>{this.state.documentInfo.type}</td>
                        </tr>
                        <tr>
                            <th>Aprašymas</th>
                            <td>{this.state.documentInfo.description}</td>
                        </tr>
                        <tr>
                            <th>Failo pavadinimas</th>
                            <td>
                                <ul>
                                    {this.state.documentInfo.filesAttachedToDocument ?
                                        this.state.documentInfo.filesAttachedToDocument.map(file => <li>
                                            <a href={'http://localhost:8181/api/files/download/' + file.identifier}
                                               target='_blank'>{file.fileName}</a>
                                            {/* mes naudojame localhost:8181/api  todel, kad react-server proxy nesuveikia kai content tipas yra nustatytas
                                    */}
                                            {/*<a href='#' onClick={() => this.downloadOneFile(file.identifier)} >{file.fileName}</a>*/}
                                        </li>)
                                        : ''
                                    }
                                </ul>
                            </td>
                        </tr>
                        <tr>
                            <th>Dokumento statusas</th>
                            <td>{this.state.documentInfo.documentState}</td>
                        </tr>

                        </tbody>
                    </table>


                    {this.state.documentInfo.documentState === 'CREATED' ?

                        <button className="btn btn-info btn-sm" onClick={this.submitDocument}>Pateikti</button>
                        : (this.state.documentInfo.documentState == 'SUBMITTED' ?
                            <div>
                                <button className="btn btn-success btn-sm mr-4"
                                        onClick={this.approveDocument}>Patvirtinti
                                </button>
                                < button className="btn btn-danger btn-sm ml-5" onClick={this.rejectDocument}>Atmesti
                                </button>
                            </div> : '')
                    }


                </div>


                {/*<h4 style={{color: 'green'}}>Description: {this.state.description}</h4>*/}
                {/*<h4 style={{color: 'green'}}>Type: {this.state.type}</h4>*/}
                {/*<h4 style={{color: 'green'}}>Failo Pavadinimas {this.state.attachedFileName}</h4>*/}
                {/*<button onClick={this.downloadFile}>Download {this.state.attachedFileName} file</button>*/}

            </React.Fragment>
        );
    }
}

export default AugisDokumentas;