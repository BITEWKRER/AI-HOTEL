package AI.hotel.mapper;

import AI.hotel.bean.Menu;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface MenuMapper {

    @Insert("insert into menu (name,money,type,path,create_at) values (#{name},#{money},#{type},#{path},#{create_at})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Boolean addMenuItem(Menu menu);

    @Update("update menu set name = #{name},money=#{money},type=#{type},path = #{path},update_at = now() where id = #{id}")
    Boolean updateMenuItem(Menu menu);

    @Delete("delete from  menu  where id = #{id} ")
    Boolean deleteMenuItem(@Param("id") int id);

    @Select("select * from menu")
    ArrayList<Menu> getMenuItems();

    @Select("select path from menu where id  = #{param1}")
    String getPathById(@Param("id") int id);

    @Select("select count(id) from menu where name LIKE  CONCAT('%',#{name},'%') ")
    Integer SearchMenuByNameCount(@Param("name") String name);

    @Select("select count(id) from menu")
    Integer getMenusCount();

    @Select("select * from menu where name LIKE CONCAT('%',#{name},'%')  limit #{start},#{end}")
    ArrayList<Menu> SearchMenuByName(@Param("name") String name, @Param("start") int start, @Param("end") int end);

    @Select("select * from menu limit #{start},#{end}")
    ArrayList<Menu> getMenus(@Param("start") Integer start, @Param("end") int end);
}

