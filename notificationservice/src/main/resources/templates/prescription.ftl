<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<p>Hi</p>
<p>Diagononis ${prescription.diagnosis}</p>
<p>Medicines</p>
    <ul>
        <#list medicineList as medicine>
            <li>Name: ${medicine.name} </li>
            <ul>Dosage: ${medicine.dosage}</ul>
            <ul>Frequency: ${medicine.frequencey}</ul>
            <ul>Remarks: ${medicine.remarks}</ul>
        </#list>
    </ul>
<p>Regards,</p>
<p>
    <em>Dr. Consultancy Team</em> <br />
</p>
</body>
</html>