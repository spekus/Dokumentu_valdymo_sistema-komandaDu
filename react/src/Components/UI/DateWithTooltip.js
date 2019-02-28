import React from 'react';
import Moment from "react-moment";

const DateWithTooltip = (props) => {
    if (props.date &&
        props.date instanceof Date &&
        props.date.toISOString() !== new Date(null).toISOString()) {
        return (
            <div className="datetooltip">
                <Moment format='YYYY-MM-DD' date={props.date}/>
                <span className="datetooltiptext">
                <Moment format='YYYY-MM-DD HH:mm:ss' date={props.date}/>
            </span>
            </div>
        );
    }
    return "-/-";
};

export default DateWithTooltip;
