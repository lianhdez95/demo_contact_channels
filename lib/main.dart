// ignore_for_file: avoid_print
import 'package:another_contact_channel/pages/battery_status_chanel_page.dart';
import 'package:another_contact_channel/pages/camera_open_channel_page.dart';
import 'package:another_contact_channel/pages/check_connection_channel_page.dart';
import 'package:another_contact_channel/pages/contact_picker_channel_page.dart';
import 'package:another_contact_channel/pages/device_info_channel_page.dart';
import 'package:another_contact_channel/pages/gallery_pick_channel_page.dart';
import 'package:another_contact_channel/pages/get_contacts_channel_page.dart';
import 'package:another_contact_channel/pages/home_page.dart';
import 'package:another_contact_channel/pages/location_channel_page.dart';
import 'package:flutter/material.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);  
  
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Channels Demo',
      debugShowCheckedModeBanner: false,
      initialRoute: '/',
      routes: {
        '/': (_) => const HomePage(),
        '/contact_channel': (_) => const GetContactsChannelPage(),
        '/battery_channel': (_) => const BatteryStatusChannelPage(),
        '/connection_channel': (_) => const CheckConnectionChannelPage(),
        '/contact_picker_channel': (_) => const ContactPickerChannelPage(),
        '/camera_open_channel': (_) => const CameraOpenChannelPage(),
        '/gallery_pick_channel': (_) => const GalleryPickChannelPage(),
        '/device_info_channel': (_) => const DeviceInfoChannelPage(),
        '/location_channel': (_) => const LocationChannelPage()
      },
    );
  }
}
