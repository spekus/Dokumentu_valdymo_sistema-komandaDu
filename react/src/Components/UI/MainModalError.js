import React from 'react';
import $ from 'jquery';


export const ShowError = (errorText) => {
    $('#MainModalErrorText').html(errorText);
    $('#MainModalError').modal('show');
}


export const showErrorObject = (errorObject) => {
    let errorText = "";
    if (errorObject.response.data.message) {
        errorText = errorObject.response.data.message;
    } else if (errorObject.message) {
        errorText = errorObject.message;
    } else {
        errorText = errorObject.toString()
    }
    ;

    ShowError(errorText);
}


const MainModalError = () => {
    return (
        <div className="modal fade" id="MainModalError" role="dialog">
            <div className="modal-dialog modal-dialog-centered" role="document">
                <div className="modal-content">
                    <div className="modal-header alert-danger">
                        <h5 className="modal-title">Klaida!</h5>
                        <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div className="modal-body" id="MainModalErrorText">
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-dismiss="modal">Uždaryti</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MainModalError;
