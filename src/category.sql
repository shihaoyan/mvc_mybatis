/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.5.49 : Database - store
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`store` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `store`;

/*Table structure for table `address` */

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
  `aid` int(11) NOT NULL AUTO_INCREMENT,
  `address_name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `address` */

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(200) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `admin` */

insert  into `admin`(`id`,`account`,`password`) values (1,'123456','123456');

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `cname` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `category` */

insert  into `category`(`cid`,`cname`) values (1,'数码产品'),(3,'家用电器'),(4,'服装饰品');

/*Table structure for table `order` */

DROP TABLE IF EXISTS `order`;

CREATE TABLE `order` (
  `oid` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `oquantity` int(11) DEFAULT NULL,
  `ototal` double DEFAULT NULL,
  `onumber` varbinary(200) DEFAULT NULL,
  PRIMARY KEY (`oid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `order` */

/*Table structure for table `product` */

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `pname` varchar(200) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `picture` varchar(200) DEFAULT NULL,
  `cid` int(11) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `product` */

/*Table structure for table `shop_cart` */

DROP TABLE IF EXISTS `shop_cart`;

CREATE TABLE `shop_cart` (
  `scid` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(200) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`scid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `shop_cart` */

/*Table structure for table `t_evaluate` */

DROP TABLE IF EXISTS `t_evaluate`;

CREATE TABLE `t_evaluate` (
  `eid` int(11) NOT NULL AUTO_INCREMENT,
  `mid` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`eid`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

/*Data for the table `t_evaluate` */

/*Table structure for table `t_music_info` */

DROP TABLE IF EXISTS `t_music_info`;

CREATE TABLE `t_music_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mid` int(11) DEFAULT NULL,
  `musicImage` varchar(255) DEFAULT NULL,
  `clickRate` int(11) DEFAULT '0',
  `downloads` int(11) DEFAULT '0',
  `user_like` int(11) DEFAULT '0',
  `user_dislike` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `t_music_info` */

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `userPassword` varchar(255) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `userImage` varchar(255) DEFAULT NULL,
  `userMood` varchar(255) DEFAULT NULL,
  `userGrade` int(11) DEFAULT '0',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

/*Table structure for table `t_user_grade` */

DROP TABLE IF EXISTS `t_user_grade`;

CREATE TABLE `t_user_grade` (
  `gid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `grade` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT '0',
  PRIMARY KEY (`gid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `t_user_grade` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(200) DEFAULT NULL,
  `username` varchar(200) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `sex` varchar(2) DEFAULT NULL,
  `phone` varchar(200) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`uid`,`account`,`username`,`password`,`sex`,`phone`,`state`) values (1,'123456','小明','123456','男','15144487965',1);

/*Table structure for table `user_address` */

DROP TABLE IF EXISTS `user_address`;

CREATE TABLE `user_address` (
  `uaid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `aid` int(11) DEFAULT NULL,
  PRIMARY KEY (`uaid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_address` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
