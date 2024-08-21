import React, { useState } from 'react';
import Button from '@mui/material/Button';
import './App.css';
import { Field, FieldHookConfig, Formik, useField, useFormik } from 'formik';
import * as Yup from 'yup';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';
import { mainuseremail } from './Login';



/**
 * This class returns the questionnaire for the study match type.
 * There are inputs and a submit button that the user can fill out
 * and interact with, and the information is then sent to the 
 * backedd usng out backeend API's endpoint.
 * Inspired by https://codesandbox.io/s/formik-v2-tutorial-final-ge1pt?file=/src/index.js and https://formik.org/docs/tutorial
 * @returns 
 */


const QuestionnaireS = () => {


  const formik = useFormik({
    initialValues: {
      name: '',
      pronouns: '',
      classYear: '',
      email: '',
      studyHabs: '',
      classes: '',
      studySpot: '',
      reasoning: '',
      // acceptanceTerm: false
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
        studyHabs: Yup.string()
          .max(5000, "Must be 100 character or less")
          .required("Required"),
        classes: Yup.string()
          .max(5000, "Must be 100 character or less")
          .required("Required"),
        studySpot: Yup.string()
          .max(5000, "Must be 100 character or less")
          .required("Required"),
        reasoning: Yup.string()
          .max(5000, "Must be 100 character or less")
          .required("Required"),
      }),
    onSubmit: values => {
        values.email = mainuseremail;
        const surveyData = JSON.stringify(values, null, 2);
        alert(surveyData)

    // send data to backend

        fetch('http://localhost:9000/getQuestionairreResponse?data-vals=' + surveyData  + '&Qtype=study')

     
    },
  });
  
  return (
    <div id="form-module">
    <form style={{margin: 100, padding: 50, position: "relative" }} onSubmit={formik.handleSubmit} aria-label="form to fill out study buddy match questionnaire">
      <label className="labelForm" htmlFor="name" aria-label="input form to enter name">Name: </label>
      <input
        id="name"
        name="name"
        type="text"
        onChange={formik.handleChange}
        onBlur={formik.handleBlur}
        value={formik.values.name}
      />

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
        value={formik.values.pronouns}
      />

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
        value={formik.values.classYear}
      />

        {formik.touched.classYear && formik.errors.classYear ? (
         <div>{formik.errors.classYear}</div>
       ) : null}


      <label className="labelForm" htmlFor="studyHabs" aria-label="input form to enter study habits">Describe your study habits: </label>
      <input
        id="studyHabs"
        name="studyHabs"
        type="text"
        onChange={formik.handleChange}
        value={formik.values.studyHabs}
      />

        {formik.touched.studyHabs && formik.errors.studyHabs ? (
         <div>{formik.errors.studyHabs}</div>
       ) : null}

      <label className="labelForm" htmlFor="classes" aria-label="input form to enter classes">What classes / kinds of classes are you taking: </label>
      <input
        id="classes"
        name="classes"
        type="text"
        onChange={formik.handleChange}
        value={formik.values.classes}
      />

        {formik.touched.classes && formik.errors.classes ? (
         <div>{formik.errors.classes}</div>
       ) : null}

      <label className="labelForm" htmlFor="studySpot" aria-label="input form to enter study spots">Favorite places to study on/near campus: </label>
      <input
        id="studySpot"
        name="studySpot"
        type="text"
        onChange={formik.handleChange}
        value={formik.values.studySpot}
      />

        {formik.touched.studySpot && formik.errors.studySpot ? (
         <div>{formik.errors.studySpot}</div>
       ) : null}

      <label className="labelForm" htmlFor="reasoning" aria-label="input form to enter reasoning">What is your reasoning for filling out this questionnaire? </label>
      <input
        id="reasoning"
        name="reasoning"
        type="text"
        onChange={formik.handleChange}
        value={formik.values.reasoning}
      />

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



export default QuestionnaireS;