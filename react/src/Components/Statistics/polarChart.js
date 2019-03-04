import React, {Component} from 'react';
import axios from "axios/index";
import DateRangePicker from '@wojtekmaj/react-daterange-picker';
import { Polar } from "react-chartjs-2";
import { MDBContainer } from "mdbreact";
import chroma from "chroma-js";
import scale from "chroma-js";
import {hsv_rainbow} from "google-palette";
import palette from "google-palette";


class Charts extends Component {

    state = {
        date: [new Date().toISOString(), new Date().toISOString()],
        approvedStatistics: [],              //Patvirtintų dokumentų statistika per nustatytą laikotarpį
        rejectedStatistics: [],              //Atmestų dokumentų statistika per nustatytą laikotarpį
        postedStatistics: [],                //Pateiktų dokumentų statistika per nustatytą laikotarpį
        userListByPostedDocs: [],            //Vartotojų sąrašas, surikiuotas pagal daugiausiai pateiktų dokumentų skaičių
            dataPolarApproved:{},           //Grafikų duomenų masyvai
            dataPolarRejected:{},
            dataPolarPosted:{},
            dataPolarUserlist:{}
    }

//########## kalendorius ##########
    onChange = date => {
        if(date!==null){
            this.setState({ date })
        }
        else { this.setState({date:''})}
    }

//########## mygtuko paspaudimas "Rodyti statistiką" iškviečia AXIOS duomenų gavimui iš back'endo ##########
    getStatistics=()=>{
        this.getApprovedData();
        this.getRejectedData();
        this.getPostedData();
        this.getUserListByPostedDocs();
    }

//########## Graikų duomenų masyvai, kurie užpildomi gaunant data iš BACKendo ##########
    dataApproved=[];
    dataRejected=[];
    dataPosted=[];
    dataUserList=[];
    backgroundColorApproved=[];
    backgroundColorRejected=[];
    backgroundColorPosted=[];
    backgroundColorUserList=[];
    labelsApproved=[];
    labelsRejected=[];
    labelsPosted=[];
    labelsUserList=[];

//########## Iškviečiant mygtuku statistiką prasivalo masyvai, kad nekaupti duomenų jose ##########
    resetState=()=>{
        this.dataApproved=[];
        this.dataRejected=[];
        this.dataPosted=[];
        this.dataUserList=[];
        this.backgroundColorApproved=[];
        this.backgroundColorRejected=[];
        this.backgroundColorPosted=[];
        this.backgroundColorUserList=[];
        this.labelsApproved=[];
        this.labelsRejected=[];
        this.labelsPosted=[];
        this.labelsUserList=[];
    }

//########## Metodai, ateinančių duomenų iš BACKENDO apdorojimui ir sukišimui į statistikos masyvus ##########
    approvedData=()=>{  
        // choose colors here http://google.github.io/palette.js/
        var seq = palette('tol-dv', 5);
        var skaicius =0;
        this.resetState();
        this.state.approvedStatistics.map(item => {
            this.dataApproved.push(parseInt(item.count));
            this.backgroundColorApproved.push("#" + seq[skaicius]);
             skaicius = skaicius + 1;
            this.labelsApproved.push(item.type)
        });
        let mydata={
            datasets:[
                {
                    data:this.dataApproved,
                    backgroundColor:this.backgroundColorApproved,
                    label: "My dataset"
                }
            ],
            labels:this.labelsApproved
        }
        this.state.dataPolarApproved=mydata;
        this.setState({state:this.state});
    }

    rejectedData=()=>{
        var seq = palette('cb-GnBu', 5);
        var skaicius =0;
        this.resetState();
        this.state.rejectedStatistics.map(item => {
            this.dataRejected.push(parseInt(item.count));
 
            // this.backgroundColorRejected.push("#" + ("000000" + Math.floor(Math.random() * 16777216).toString(16)).substr(-6));
            this.backgroundColorRejected.push("#" + seq[skaicius]);
            skaicius = skaicius + 1;
            this.labelsRejected.push(item.type)
        });
        let mydata={
            datasets:[
                {
                    data:this.dataRejected,
                    backgroundColor:this.backgroundColorRejected,
                    label: "My dataset"
                }
            ],
            labels:this.labelsRejected
        }
        this.state.dataPolarRejected=mydata;
        this.setState({state:this.state});
    }

    postedData=()=>{
        var seq2 = palette('tol-dv', 10);
        var skaicius = 0;
        this.resetState();
        this.state.postedStatistics.map(item => {
            this.dataPosted.push(parseInt(item.count));

            // this.backgroundColorPosted.push("#" + ("000000" + Math.floor(Math.random() * 16777216).toString(16)).substr(-6));
            this.backgroundColorPosted.push("#" + seq2[skaicius]);
            skaicius = skaicius + 1;
            this.labelsPosted.push(item.type)
        });
        let mydata={
            datasets:[
                {
                    data:this.dataPosted,
                    backgroundColor:this.backgroundColorPosted,
                    label: "My dataset"
                }
            ],
            labels:this.labelsPosted
        }
        this.state.dataPolarPosted=mydata;
        this.setState({state:this.state});
    }

    userListData=()=>{
        var seq = palette('cb-GnBu', 7);
        var skaicius =0;
        this.resetState();
        this.state.userListByPostedDocs.map(item => {
            this.dataUserList.push(parseInt(item.count));
            // this.backgroundColorUserList.push("#" + ("000000" + Math.floor(Math.random() * 16777216).toString(16)).substr(-6));
            this.backgroundColorUserList.push("#" + seq[skaicius]);
            skaicius = skaicius + 1;
            this.labelsUserList.push(item.type)
        });
       let mydata={
            datasets:[
                {
                    data:this.dataUserList,
                    backgroundColor:this.backgroundColorUserList,
                    label: "My dataset"
                }
            ],
                labels:this.labelsUserList
        }
        this.state.dataPolarUserlist=mydata;
        this.setState({state:this.state});
    }

//########## Duomenų gavimas iš BACKENDO ##########

    getApprovedData=()=>{
        axios({
            method: 'get',
            url: '/api/statistics/approved-docs',
            params: {
                startDate: this.state.date[0],
                endDate: this.state.date[1]
            },
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        })
            .then(response => {
                this.setState({approvedStatistics: response.data});
                console.log("response Approved come"+response.data)
            })
            .then(this.approvedData)
            .catch(error => {
                console.log("Klaida iš statistikos kontrollerio APPROVED-DOCS" + error.message)
            })
    }

    getRejectedData=()=>{
        axios({
            method: 'get',
            url: '/api/statistics/rejected-docs',
            params: {
                startDate: this.state.date[0],
                endDate: this.state.date[1]
            },
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        })
            .then(response => {
                this.setState({rejectedStatistics: response.data});
                console.log("response Rejected come"+response.data)
            })
            .then(this.rejectedData)
            .catch(error => {
                console.log("Klaida iš statistikos kontrollerio REJECTED-DOCS" + error.message)
            })
    }

    getPostedData=()=>{
        axios({
            method: 'get',
            url: '/api/statistics/posted-docs',
            params: {
                startDate: this.state.date[0],
                endDate: this.state.date[1]
            },
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        })
            .then(response => {
                this.setState({postedStatistics: response.data});
                console.log("response Posted come"+response.data)
            })
            .then(this.postedData)
            .catch(error => {
                console.log("Klaida iš statistikos kontrollerio POSTED-DOCS" + error.message)
            })
    }

    getUserListByPostedDocs=()=>{
        axios({
            method: 'get',
            url: '/api/statistics/userlist-by-posted-docs',
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        })
            .then(response => {
                this.setState({userListByPostedDocs: response.data});
                console.log(response.data)
            })
            .then(this.userListData)
            .catch(error => {
                console.log("Klaida iš statistikos kontrollerio USERLIST-BY-POSTED-DOCS" + error.message)
            })
    }

    render(){
        let chartApproved=<MDBContainer>
            <Polar data={this.state.dataPolarApproved} options={{ responsive: true }} />
        </MDBContainer>
        let chartRejected=<MDBContainer>
            <Polar data={this.state.dataPolarRejected} options={{ responsive: true }} />
        </MDBContainer>
        let chartPosted=<MDBContainer>
            <Polar data={this.state.dataPolarPosted} options={{ responsive: true }} />
        </MDBContainer>
        let userList=<MDBContainer>
            <Polar data={this.state.dataPolarUserlist} options={{ responsive: true }} />
        </MDBContainer>
        return(
            <div className="container">
                <h5>Laiko intervalas</h5>
                <div className='shadow p-3 mb-5 bg-white rounded'>
                    <div className="row">
                        <div className="col-md-4">
                            <DateRangePicker onChange={this.onChange} value={this.state.date}/>
                        </div>
                        <div className="col-md-4">
                            <button className="btn button1 btn-sm" onClick={this.getStatistics}>Išvesti statistiką</button>
                        </div>
                    </div>
                </div>

                <div className='shadow p-3 mb-5 bg-white rounded'>
                    <div className="row">
                        <div className="col-md-6">
                            <h5>Patvirtintų dokumentų sąrašas</h5>
                            {chartApproved}
                        </div>

                        <div className="col-md-6">
                            <h5>Atmestų dokumentų sąrašas</h5>
                            {chartRejected}
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-6">
                            <h5>Pateiktų dokumentų sąrašas</h5>
                            {chartPosted}
                        </div>

                        <div className="col-md-6">
                            <h5>Dažniausiai pateikiančių dokumentus vartotojų sąrašas</h5>
                            {userList}
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default Charts;


