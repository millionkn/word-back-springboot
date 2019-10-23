package com.example.demo.typeHandler;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Blob.class)
@MappedJdbcTypes(JdbcType.BLOB)
public class BlobTypeHandler extends BaseTypeHandler<Blob> {
  public void setNonNullParameter(PreparedStatement ps, int i, Blob parameter, JdbcType jdbcType) throws SQLException {
    ps.setBinaryStream(i, parameter.getBinaryStream());
  }

  @Override
  public Blob getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return rs.getBlob(columnName);
  }

  @Override
  public Blob getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return rs.getBlob(columnIndex);
  }

  @Override
  public Blob getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return cs.getBlob(columnIndex);
  }
}