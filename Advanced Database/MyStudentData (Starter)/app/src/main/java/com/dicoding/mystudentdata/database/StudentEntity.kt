package com.dicoding.mystudentdata.database

import androidx.room.*

@Entity
data class Student(
    @PrimaryKey             //  |== studentId ==|== name ==|== univId ==|
    val studentId: Int,     //  |               |          |            |
    val name: String,       //  |               |          |            |
    val univId: Int,         //  |===============|==========|============|
    @ColumnInfo(defaultValue = "false")
    val isGraduate: Boolean? = false
    )

@Entity
data class University(
    @PrimaryKey             //  |== universityId ==|== name ==|
    val universityId: Int,  //  |                  |          |
    val name: String        //  |==================|==========|
)

@Entity
data class Course(
    @PrimaryKey             //  |== courseId ==|== name ==|
    val courseId: Int,      //  |              |          |
    val name: String        //  |==============|==========|
)

@Entity(primaryKeys = ["sId", "cId"])
data class CourseStudentCrossRef(
    val sId: Int,               //  |== sId ==|== cId ==|
    @ColumnInfo(index = true)   //  |         |         |
    val cId: Int                //  |=========|=========|
)

data class StudentAndUniversity(
    @Embedded
    val student: Student,

    @Relation(
        parentColumn = "univId",
        entityColumn = "universityId"
    )
    val university: University? = null
)

data class UniversityAndStudent(
    @Embedded
    val university: University,

    @Relation(
        parentColumn = "universityId",
        entityColumn = "univId"
    )
    val student: List<Student>
)

data class StudentWithCourse(
    @Embedded
    val studentAndUniversity: StudentAndUniversity,

    @Relation(
        parentColumn = "studentId",
        entityColumn = "courseId",
        entity = Course::class,
        associateBy = Junction(
            value = CourseStudentCrossRef::class,
            parentColumn = "sId",
            entityColumn = "cId"
        )
    )
    val course: List<Course>
)