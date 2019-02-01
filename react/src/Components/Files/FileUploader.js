import React, {Component} from 'react';
import FileSaver from 'file-saver';
import axios from 'axios';

// run npm install file-saver --save

export default class FileUploader extends Component {

     state = {
        file: '',
        error: '',
        msg: '',
        savedFileIdentifier: ''
    }


    uploadFile = (event) => {
        event.preventDefault();
        this.setState({error: '', msg: ''});

        if (!this.state.file) {
            this.setState({error: 'Please upload a file.'})
            return;
        }

        if (this.state.file.size >= 2000000) {
            this.setState({error: 'File size exceeds limit of 2MB.'})
            return;
        }

        let data = new FormData();
        data.append('file', this.state.file);
        data.append('name', this.state.file.name);

        fetch('http://localhost:8181/api/files', {
            method: 'POST',
            body: data
        })
            .then(response => {
                this.setState({error: '', msg: 'Sucessfully uploaded file'});
                // callback(response.body);
                console.log(response.body);
                console.log(response)
                console.log(response.bodyUsed)

                // to read response object from controller
                if (response.ok) {
                    response.json().then(json => {
                      console.log(json);
                      var identifier = json.text;
                      this.setState(
                          {savedFileIdentifier: identifier});
                      console.log("the file is saved to the state - " +
                          this.state.savedFileIdentifier);


                    //   console.log(identifier[0].text);

                    });
                }
            })
            .catch(err => {
                    this.setState({error: err.message})
                }
            );


    }


    onFileChange = (event) => {
        this.setState({
            file: event.target.files[0]
        });
    }


    render() {
        return (
            <React.Fragment>

                {/* Main content */}
                <div>
                    <div>
                        <h4 className="my-4" align="center">
                            Naujo dokumento sukūrimas
                        </h4>


                        <form classname="form1 col-md-9" onSubmit={this.handleSubmit}>
                            <div className="row">
                                <div className="col-md-2"></div>
                                <div className="col-md-9">
                                    <div className="form-group col-md-10">
                                        <label htmlFor="exampleFormControlInput1">Pavadinimas</label>
                                        <input type="text" className="form-control" id="exampleFormControlInput1"
                                               placeholder="Įveskite dokumento pavadinimą" value={this.state.title}
                                               onChange={this.handleChangeTitle}/>
                                    </div>
                                    <div className="form-group col-md-10">
                                        <label htmlFor="exampleFormControlSelect1">Dokumento tipas</label>
                                        <select className="form-control" id="exampleFormControlSelect1">
                                            <option value="Bendri">1.Bendri</option>
                                            <option value="Darbuotojų prašymai">2.Darbuotojų prašymai</option>
                                            <option value="Projektų dokumentacija">3.Projektų dokumentacija</option>
                                            <option value="Buhalteriniai dokumentai">4.Buhalteriniai dokumentai</option>
                                            <option value="Kita">5.Kita</option>
                                        </select>
                                    </div>
                                    <div className="form-group col-md-10">
                                        <label htmlFor="exampleFormControlTextarea1">Aprašymas</label>
                                        <textarea className="form-control" id="exampleFormControlTextarea1" rows="3"
                                                  placeholder="Įveskite trumpą dokumento aprašymą"
                                                  value={this.state.description}
                                                  onChange={this.handleChangeDescription}></textarea>
                                    </div>

                                    <div className="form-group col-md-9 mt-4">
                                        <input onChange={this.onFileChange} type="file"></input><br/>
                                        <h4 style={{color: 'red'}}>{this.state.error}</h4>
                                        <h4 style={{color: 'green'}}>{this.state.msg}</h4>

                                    </div>
                                </div>
                            </div>
                        </form>

                        <React.Fragment>

                            <div className="text-center">
                                <button type="submit" className="btn btn-danger my-4"
                                        onClick={this.uploadFile}>Išsaugoti
                                </button>
                            </div>

                        </React.Fragment>


                    </div>

                </div>
            </React.Fragment>
        );
    }
}


