package ru.sgu.univer.app.writers;

import android.media.Rating;
import android.os.Environment;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.sgu.univer.app.objects.Group;
import ru.sgu.univer.app.objects.RatingTable;
import ru.sgu.univer.app.objects.Student;
import ru.sgu.univer.app.providers.CourseProvider;
import ru.sgu.univer.app.providers.GroupProvider;
import ru.sgu.univer.app.providers.LessonTypeProvider;
import ru.sgu.univer.app.providers.MegaProvider;
import ru.sgu.univer.app.providers.PerevodProvider;
import ru.sgu.univer.app.providers.RatingProvider;
import ru.sgu.univer.app.providers.StudentProvider;

public class ExcelParser {

    private static final String LOG_TAG = "SD_LOG";
    private static final String DIR_SD = "ExcelFiles";
    public static final String COURSES = "courses";
    public static final String COURSE_MAP = "courseMap";
    public static final String COURSE_UID = "uid";
    public static final String GROUPS = "groups";
    public static final String GROUP_MAP = "groupMap";
    public static final String GROUP_UID = "groupUid";
    public static final String TWO = "two";
    public static final String THREE = "three";
    public static final String FOUR = "four";
    public static final String FIVE = "five";
    public static final String RATING = "rating";
    public static final String STUDENTS = "students";
    public static final String STUDENT_MAP = "studentMap";
    public static final String STUDENT_UID = "studentUid";

    private static File createFile(String fileName) {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return null;
        }
        Log.d(LOG_TAG, "SD-карта: " + Environment.getExternalStorageDirectory());

        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        if (!sdPath.mkdirs()) {
            Log.d(LOG_TAG, "Не могу создать директорию");
        }
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(fileName);
        // открываем поток для записи
        try {
            sdFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sdFile;
    }

    public static void toExcel(int courseId, int groupId, String fileName) {

        RatingTable ratingTable = RatingProvider.getById(courseId, groupId);
        List<Student> students = StudentProvider.getStudentsByGroupId(groupId);

        File file = createFile(fileName);

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();

        HSSFRow firstRow = sheet.createRow(0);
        firstRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue("Имя");

        for (int i = 0; i < ratingTable.lessons.size(); i++) {
            HSSFCell cell = firstRow.createCell(i + 1, HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(ratingTable.lessons.get(i).toString());
        }

        firstRow.createCell(ratingTable.lessons.size() + 1, HSSFCell.CELL_TYPE_STRING).setCellValue("Сумма");
        firstRow.createCell(ratingTable.lessons.size() + 2, HSSFCell.CELL_TYPE_STRING).setCellValue("Оценка");

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue(student.toString());

            int count = 1;
            for (Integer mark : ratingTable.getRatingByStudentId(student.id)) {
                String s = "";
                if(mark == -1)
                    s = "Н";
                if(mark > 0)
                    s = String.valueOf(mark);
                row.createCell(count, HSSFCell.CELL_TYPE_STRING).setCellValue(s);
                count++;
            }

            int sum = ratingTable.getSumStudentId(student.id);
            row.createCell(count, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(sum);
            count++;
            row.createCell(count, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(PerevodProvider.getOchen(sum));
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
            Log.d(LOG_TAG, "Файл записан на SD: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fromExcel(int courseId, int groupId, String fileName) {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        Log.d(LOG_TAG, "SD-карта: " + Environment.getExternalStorageDirectory());

        // получаем путь к SD
        InputStream in = null;
        HSSFWorkbook wb = null;
        try {
            in = new FileInputStream(fileName);
            wb = new HSSFWorkbook(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<Integer, Integer> studentMap = new HashMap<Integer, Integer>();

        Sheet sheet = wb.getSheetAt(0);

        StudentProvider.removeStudentByGroupId(groupId);
        for(int i = 1; i <= sheet.getLastRowNum(); i++) {
            String[] sp = sheet.getRow(i).getCell(0).getStringCellValue().split(" ");
            Student student = StudentProvider.add(sp[1], sp[0], "", "", "", groupId);
            studentMap.put(i - 1, student.id);
        }

        RatingProvider.add(courseId, groupId);
        RatingTable rating = RatingProvider.getById(courseId, groupId);


        for(int i = 1; i < sheet.getRow(0).getLastCellNum() - 2; i++) {
            String cell = sheet.getRow(0).getCell(i).getStringCellValue();
            Log.d(LOG_TAG, "Name date: " + cell);
            String name = cell.split(" ")[0];
            String date = cell.split(" ")[1];
            rating.addColumn(LessonTypeProvider.getByName(name).id, date);
        }

        for(int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            for(int j = 1; j < row.getLastCellNum() - 2; j++) {
                int val = -2;
                String cell = row.getCell(j).getStringCellValue();
                Log.d(LOG_TAG, "import i " + i + " j " + j + " cell " + cell);
                if("".equals(cell)) {
                    val = -2;
                }
                if("Н".equals(cell)) {
                    val = -1;
                }
                try {
                    val = Integer.valueOf(cell);
                } catch (NumberFormatException e) {
                }
                rating.rating.get(studentMap.get(i - 1)).set(j - 1, val);
            }
        }
        Log.d(LOG_TAG, "Added rating table while import. size: " + rating.rating.size() + " " + rating.lessons.size());


    }

    public static void toBackUp(String fileName) {

        File file = createFile(fileName);
        MegaProvider backup = new MegaProvider();

        backup.megaMap.put(CourseProvider.class, new HashMap<String, Object>());
        backup.megaMap.get(CourseProvider.class).put(COURSES, CourseProvider.courses);
        backup.megaMap.get(CourseProvider.class).put(COURSE_MAP, CourseProvider.courseMap);
        backup.megaMap.get(CourseProvider.class).put(COURSE_UID, CourseProvider.uid);

        backup.megaMap.put(GroupProvider.class, new HashMap<String, Object>());
        backup.megaMap.get(GroupProvider.class).put(GROUPS, GroupProvider.groups);
        backup.megaMap.get(GroupProvider.class).put(GROUP_MAP, GroupProvider.groupMap);
        backup.megaMap.get(GroupProvider.class).put(GROUP_UID, GroupProvider.uid);

        backup.megaMap.put(PerevodProvider.class, new HashMap<String, Object>());
        backup.megaMap.get(PerevodProvider.class).put(TWO, PerevodProvider.two);
        backup.megaMap.get(PerevodProvider.class).put(THREE, PerevodProvider.three);
        backup.megaMap.get(PerevodProvider.class).put(FOUR, PerevodProvider.four);
        backup.megaMap.get(PerevodProvider.class).put(FIVE, PerevodProvider.five);

        backup.megaMap.put(RatingProvider.class, new HashMap<String, Object>());
        backup.megaMap.get(RatingProvider.class).put(RATING, RatingProvider.map);

        backup.megaMap.put(StudentProvider.class, new HashMap<String, Object>());
        backup.megaMap.get(StudentProvider.class).put(STUDENTS, StudentProvider.students);
        backup.megaMap.get(StudentProvider.class).put(STUDENT_MAP, StudentProvider.studentsMap);
        backup.megaMap.get(StudentProvider.class).put(STUDENT_UID, StudentProvider.uid);

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(fileName);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(backup);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fromBackUp(String fileName) {

        FileInputStream fis = null;
        ObjectInputStream oin = null;
        MegaProvider backup = null;
        try {
            fis = new FileInputStream(fileName);
            oin = new ObjectInputStream(fis);
            backup = (MegaProvider) oin.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("Log", "from bvack up " + fileName + " provider " + backup.megaMap.size());

        CourseProvider.courses.addAll((List<ru.sgu.univer.app.objects.Course>) backup.megaMap.get(CourseProvider.class).get(COURSES));
        CourseProvider.courseMap.putAll((Map<Integer, ru.sgu.univer.app.objects.Course>) backup.megaMap.get(CourseProvider.class).get(COURSE_MAP));
        CourseProvider.uid = (Integer) backup.megaMap.get(CourseProvider.class).get(COURSE_UID);

        GroupProvider.groups.addAll((List<Group>)backup.megaMap.get(GroupProvider.class).get(GROUPS));
        GroupProvider.groupMap.putAll((Map<Integer, List<Group>>) backup.megaMap.get(GroupProvider.class).get(GROUP_MAP));
        GroupProvider.uid = (Integer)backup.megaMap.get(GroupProvider.class).get(GROUP_UID);

        PerevodProvider.two = (Integer)backup.megaMap.get(PerevodProvider.class).get(TWO);
        PerevodProvider.three = (Integer)backup.megaMap.get(PerevodProvider.class).get(THREE);
        PerevodProvider.four = (Integer)backup.megaMap.get(PerevodProvider.class).get(FOUR);
        PerevodProvider.five = (Integer)backup.megaMap.get(PerevodProvider.class).get(FIVE);

        RatingProvider.map.putAll((Map<Integer, Map<Integer, RatingTable>>)backup.megaMap.get(RatingProvider.class).get(RATING));

        StudentProvider.students.addAll((List<Student>)backup.megaMap.get(StudentProvider.class).get(STUDENTS));
        StudentProvider.studentsMap.putAll((Map<Integer, List<Student>>) backup.megaMap.get(StudentProvider.class).get(STUDENT_MAP));
        StudentProvider.uid = (Integer)backup.megaMap.get(StudentProvider.class).get(STUDENT_UID);
    }

}
