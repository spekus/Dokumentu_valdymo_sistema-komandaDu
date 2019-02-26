import React from 'react';
import Moment from "react-moment";

const DateWithTime = (props) => {
      if (props.date &&
          props.date instanceof Date &&
          props.date.toISOString() !== new Date(null).toISOString()) {
          return (
              <React.Fragment>
              <Moment date={props.date} format="YYYY-MM-DD HH:mm"/>
              </React.Fragment>
          )
      }
      return "-/-"
};

export default DateWithTime;
