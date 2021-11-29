# SQL Analyzer

[![Java CI](https://github.com/tamurashingo/sql-analyzer/actions/workflows/ci.yml/badge.svg)](https://github.com/tamurashingo/sql-analyzer/actions/workflows/ci.yml)
[![License: MIT](http://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)



A library for SQL. 

## Required

- Java 1.8+


```xml
<dependency>
  <groupId>io.github.tamurashingo.sqlanalyzer</groupId>
  <artifactId>sql-analyzer</artifactId>
  <version>0.0.1</version>
</dependency>
```


## Usage


Write SQL with named parameter.

```
String sql = "SELECT COUNT(*) FROM tbl WHERE id = :id AND CREATED_AT > :yesterday"
```

Put parameters to `Map<String, Object>`.

```java
Map<String,Object> params = new HashMap<String, Object>(){{
    put("yesterday", "2021-11-25");
    put("id", 3);
}};
```


Convert SQL with `SQLAnalyzer`.

```java
SQLAnalyzer analyzer = new SQLAnalyzer();
String convertSQL = analyzer.analyzeSQL(sql);
// converted SQL
// "SELECT COUNT(*) FROM tbl WHRE id = ? AND CREATED_AT > ?"
```

Convert parameters.

```java
Object[] convertParams = analyzer.analyzeParams(params);
// converted parameters
// Object[] { 3, "2021-11-25" }
```

Execute query.

```java
PreparedStatement stmt = conn.prepareStatement(convertSQL);
int ix = 0;
for (Object param: convertParams) {
    ix++;
    stmt.setObject(ix, param);
}
stmt.executeQuery();
```


## LICENSE

Copyright &copy 2021 tamura shingo
Licensed under the [MIT License][MIT].

[MIT]: https://www.opensource.org/licenses/mit-license.php
