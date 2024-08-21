import React, { useState } from 'react';
import Button from '@mui/material/Button';
import './App.css';
import { Formik, useFormik } from 'formik';
import * as Yup from 'yup';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';
import { mainuseremail } from './Login';


/**
 * This class returns the questionnaire for the date match type.
 * There are inputs and a submit button that the user can fill out
 * and interact with, and the information is then sent to the 
 * backedd usng out backeend API's endpoint.
 * Inspired by https://codesandbox.io/s/formik-v2-tutorial-final-ge1pt?file=/src/index.js and https://formik.org/docs/tutorial
 * @returns 
 */

const QuestionnaireD = () => {


  const formik = useFormik({
    initialValues: {
      name: '',
      pronouns: '',
      classYear: '',
      email: '',
      perfDate: '',
      expectations: '',
      passions: '',
      reasoning: ''
    },
    validationSchema: Yup.object({
        name: Yup.string()
          .max(15, "Must be 15 characters or less")
          .required("Required"),
        pronouns: Yup.string()
          .max(15, "Must be 15 characters or less")
          .required("Required"),
        classYear: Yup.string()
          .max(4, "Must be a valid class year")
          .min(4, "Must be a valid class year")
          .required("Required"),
        perfDate: Yup.string()
          .max(5000, "Must be 100 character or less")
          .required("Required"),
        expectations: Yup.string()
          .max(5000, "Must be 100 character or less")
          .required("Required"),
        passions: Yup.string()
          .max(5000, "Must be 100 character or less")
          .required("Required"),
        reasoning: Yup.string()
          .max(5000, "Must be 100 character or less")
          .required("Required"),
      }),
    onSubmit: values => {
        values.email = mainuseremail;
        const surveyData = JSON.stringify(values, null, 2)
        alert(surveyData)

    // send data to backend

      fetch('http://localhost:9000/getQuestionairreResponse?data-vals=' + surveyData + '&Qtype=date')

       

     
    },
  });
  
  return (
    <div id="form-module">
    <form style={{margin: 100, padding: 50, position: "relative" }} onSubmit={formik.handleSubmit} aria-label="form to fill out date match questionnaire">
      <label className="labelForm" htmlFor="name" aria-label="input form to enter name">Name: </label>
      <input
        id="name"
        name="name"
        type="text"
        onChange={formik.handleChange}
        onBlur={formik.handleBlur}
        value={formik.values.name} />

      {formik.touched.name && formik.errors.name ? (
        <div>{formik.errors.name}</div>
      ) : null}

      <label className="labelForm" htmlFor="pronouns" aria-label="input form to enter pronouns">Pronouns: </label>
      <input
        id="pronouns"
        name="pronouns"
        type="text"
        onChange={formik.handleChange}
        onBlur={formik.handleBlur}
        value={formik.values.pronouns} />

      {formik.touched.pronouns && formik.errors.pronouns ? (
        <div>{formik.errors.pronouns}</div>
      ) : null}


      <label className="labelForm" htmlFor="classYear" aria-label="input form to enter class year">Class year: </label>
      <input
        id="classYear"
        name="classYear"
        type="text"
        onChange={formik.handleChange}
        onBlur={formik.handleBlur}
        value={formik.values.classYear} />

      {formik.touched.classYear && formik.errors.classYear ? (
        <div>{formik.errors.classYear}</div>
      ) : null}


      <label className="labelForm" htmlFor="perfDate" aria-label="input form to enter date details">Describe your perfect date: </label>
      <input
        id="perfDate"
        name="perfDate"
        type="text"
        onChange={formik.handleChange}
        value={formik.values.perfDate} />

      {formik.touched.perfDate && formik.errors.perfDate ? (
        <div>{formik.errors.perfDate}</div>
      ) : null}

      <label className="labelForm" htmlFor="expectations" aria-label="input form to enter expectations">Describe your expectations in a romantic relationship: </label>
      <input
        id="expectations"
        name="expectations"
        type="text"
        onChange={formik.handleChange}
        value={formik.values.expectations} />

      {formik.touched.expectations && formik.errors.expectations ? (
        <div>{formik.errors.expectations}</div>
      ) : null}

      <label className="labelForm" htmlFor="passions" aria-label="input form to enter passions">Talk about something you are passionate about: ) </label>
      <input
        id="passions"
        name="passions"
        type="text"
        onChange={formik.handleChange}
        value={formik.values.passions} />

      {formik.touched.passions && formik.errors.passions ? (
        <div>{formik.errors.passions}</div>
      ) : null}

      <label className="labelForm" htmlFor="reasoning" aria-label="input form to enter reasoning">What is your reasoning for filling out this questionnaire?</label>
      <input
        id="reasoning"
        name="reasoning"
        type="text"
        onChange={formik.handleChange}
        value={formik.values.reasoning} />

      {formik.touched.reasoning && formik.errors.reasoning ? (
        <div>{formik.errors.reasoning}</div>
      ) : null}


      <p>By submitting this form, I consent to the analysis and storage of my data, as well as the sharing of my name, pronouns, and email with my
    matches.</p>

            <button className="labelForm" type="submit" aria-label="submit button">Submit</button>
    
      </form>
      </div>
  
  );
};

export default QuestionnaireD;


