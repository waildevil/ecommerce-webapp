import React, { useState , useMemo } from 'react';
import Select from 'react-select'
import countryList from 'react-select-country-list'

function CountrySelector({ value, onChange }) {
    const options = useMemo(() => countryList().getData(), []);
    const selectedValue = options.find(option => option.label === value);
  
    const changeHandler = (selectedOption) => {
      onChange(selectedOption);  // This will call the parent's handleCountryChange
    };
  
    return <Select options={options} value={selectedValue} onChange={changeHandler} />;
  }
  
  export default CountrySelector