<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/ea/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/ea/core" %>

<html>
<head>
  <title>JSTL: SQL action examples</title>
</head>
<body bgcolor="#FFFFFF">

<h1>SQL Query Execution using an iterator</h1>


<!-- NOTE: the sql:driver tag is for prototyping and simple applications. You should really use a DataSource object instead --!>

<sql:driver
  var="example"
  driver="$myDbDriver"
  jdbcURL="$myDbUrl"
/>

<sql:transaction dataSource="$example">
  <sql:update var="newTable" dataSource="$example">
    create table mytable (
      nameid int not null,
      name varchar(80) null,
      constraint pk_mytable primary key (nameid)
    )
  </sql:update>
</sql:transaction>

  <sql:update var="updateCount" dataSource="$example">
    INSERT INTO mytable VALUES (1,'Paul Oakenfold')
  </sql:update>
  <sql:update var="updateCount" dataSource="$example">
    INSERT INTO mytable VALUES (2,'Timo Maas')
  </sql:update>
  <sql:update var="updateCount" dataSource="$example">
    INSERT INTO mytable VALUES (3,'Paul van Dyk')
  </sql:update>


<sql:query var="deejays" dataSource="$example">
  SELECT * FROM mytable
</sql:query>

<hr>

<h2>Iterating on each Row using the MetaData</h2>

<table border="1">
  <c:forEach var="rows" begin="1" items="$deejays.rows">
    <tr>
      <td> id: <c:expr value="$rows.get('nameid')"/> </td>
      <td> name: <c:expr value="$rows.get('name')"/> </td>
    </tr>
  </c:forEach>
</table>

<hr>

<h2>Iterating on each Column getting the MetaData</h2>

<c:forEach var="metaData" begin="1" items="$deejays.metaData.columns">
  metaData: <c:expr value="$metaData.name"/> <br>
</c:forEach>

<hr>

<h2>Iterating over each Row of the result</h2>

<table border="1">
  <c:forEach var="rows" begin="1" items="$deejays.rows">
    <tr>
      <c:forEach var="column" begin="1" items="$rows.columns">
        <td><c:expr value="$column"/></td>
      </c:forEach>
    </tr>
  </c:forEach>
</table>

<hr>

<h2>Iterating over Columns without knowing the name or index</h2>

<table border="1">
  <c:forEach var="rows" begin="1" items="$deejays.rows">
      <c:forEach var="column" begin="1" items="$rows.columns">
  <tr>
        <td>Name: <c:expr value="$column.name"/></td>
        <td>Value: <c:expr value="$column"/></td>
  </tr>
      </c:forEach>
  </c:forEach>
</table>

<hr>

<h2>Putting it all together</h2>

<table border="1">
  <tr>
    <c:forEach var="metaData" begin="1" items="$deejays.metaData.columns">
      <th><c:expr value="$metaData.name"/> </th>
    </c:forEach>
  </tr>
  <c:forEach var="rows" begin="1" items="$deejays.rows">
    <tr>
      <c:forEach var="column" begin="1" items="$rows.columns">
        <td><c:expr value="$column"/></td>
      </c:forEach>
    </tr>
  </c:forEach>
</table>

<sql:update var="newTable" dataSource="$example">
  drop table mytable
</sql:update>


</body>
</html>