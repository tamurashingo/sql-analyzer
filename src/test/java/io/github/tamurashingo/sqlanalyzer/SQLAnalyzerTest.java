package io.github.tamurashingo.sqlanalyzer;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAnalyzerTest {

    @Test
    void analyze0() {
        String sql = "select count(*) from tbl";
        Map<String, Object> params = new HashMap<String, Object>();
        SQLAnalyzer analyzer = new SQLAnalyzer();

        assertEquals("select count(*) from tbl", analyzer.analyzeSQL(sql), "convert nothing");
        assertArrayEquals(new Object[]{}, analyzer.analyzeParams(params), "no parameters");
    }

    @Test
    void analyze1() {
        String sql = "select count(*) from tbl where id = :id";
        Map<String, Object> params = new HashMap<String, Object>(){{
            put("id", 3);
        }};
        SQLAnalyzer analyzer = new SQLAnalyzer();

        assertEquals("select count(*) from tbl where id = ?", analyzer.analyzeSQL(sql), ":id converted to ?");
        assertArrayEquals(new Object[]{ 3 }, analyzer.analyzeParams(params), "parameter is 3");
    }

    @Test
    void analyze2() {
        String sql = "select count(*) from tbl where start_date < :today and end_date > :today";
        Map<String, Object> params = new HashMap<String, Object>(){{
            put("today", "2021-11-26");
        }};
        SQLAnalyzer analyzer = new SQLAnalyzer();

        assertEquals("select count(*) from tbl where start_date < ? and end_date > ?", analyzer.analyzeSQL(sql), ":today converted to ?");
        assertArrayEquals(new Object[]{ "2021-11-26", "2021-11-26"}, analyzer.analyzeParams(params), "parameters are '2021-11-26', '2021-11-26'");
    }

    @Test
    void analyze3() {
        String sql = "select count(*) from tbl where id = :id and start_date < :today and end_date > :today";
        Map<String, Object> params = new HashMap<String, Object>(){{
            put("id", 4);
            put("today", "2021-11-26");
        }};
        SQLAnalyzer analyzer = new SQLAnalyzer();

        assertEquals("select count(*) from tbl where id = ? and start_date < ? and end_date > ?", analyzer.analyzeSQL(sql), ":id, :today converted to ?");
        assertArrayEquals(new Object[]{ 4, "2021-11-26", "2021-11-26"}, analyzer.analyzeParams(params), "parameters are 4, '2021-11-26', '2021-11-26'");
    }

    @Test
    void analyze4() {
        String sql = "select count(*), 'this is string literal :notparam' from tbl where id = :id";
        Map<String, Object> params = new HashMap<String, Object>(){{
            put("id", 5);
        }};
        SQLAnalyzer analyzer = new SQLAnalyzer();

        assertEquals("select count(*), 'this is string literal :notparam' from tbl where id = ?", analyzer.analyzeSQL(sql), "':notparam' in string literal not converted");
        assertArrayEquals(new Object[]{ 5 }, analyzer.analyzeParams(params));
    }

    @Test
    void analyze5() {
        String sql = "select count(*) from tbl where id = :id";
        Map<String, Object> params = new HashMap<String, Object>(){{
            put("today", "2021-11-26");
        }};
        SQLAnalyzer analyzer = new SQLAnalyzer();

        assertEquals("select count(*) from tbl where id = ?", analyzer.analyzeSQL(sql));
        assertArrayEquals(new Object[]{ null }, analyzer.analyzeParams(params), "unmatched named parameters and parameters");
    }


}
