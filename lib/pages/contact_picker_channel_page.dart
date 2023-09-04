// ignore_for_file: avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class ContactPickerChannelPage extends StatefulWidget {
  const ContactPickerChannelPage({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _ContactPickerChannelPageState createState() =>
      _ContactPickerChannelPageState();
}

class _ContactPickerChannelPageState extends State<ContactPickerChannelPage> {
  static const MethodChannel _channel = MethodChannel('com.example.contact_picker');

  String selectedContact = '';
  String selectedPhoneNumber = '';

  Future<void> openContactPicker() async {
    try {
      List<dynamic>? result = await _channel.invokeMethod('openContactPicker');
      if (result != null) {
        setState(() {
          selectedContact = result[0];
          selectedPhoneNumber = result[1];
        });
      }
    } catch (e) {
      print('Error opening contact picker: $e');
    }
  }

  Future<bool> requestContactPermission() async {
    try {
      return await _channel.invokeMethod('requestContactPermission');
    } catch (e) {
      print('Error requesting contact permission: $e');
      return false;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Contact Picker'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            customLabel(
                selectedContact,
                const Icon(
                  Icons.account_circle_outlined,
                  color: Colors.blue,
                  size: 30,
                )),

            const SizedBox(height: 10),

            //en este customLabel quiero mostrar el número de teléfono de ese contacto
            customLabel(
                selectedPhoneNumber,
                const Icon(
                  Icons.phone_rounded,
                  color: Colors.blue,
                  size: 30,
                )),

            const SizedBox(height: 20),
            ElevatedButton(
              style: ButtonStyle(
                fixedSize: MaterialStateProperty.all<Size>(const Size(180, 50)),
                shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                  RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10.0),
                  ),
                ),
              ),
              child: const Text('Choose contact',
                  style: TextStyle(fontSize: 20), textAlign: TextAlign.center),
              onPressed: () {
                openContactPicker();
              },
            ),
          ],
        ),
      ),
    );
  }

  Widget customLabel(String text, Icon icon) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        icon,
        Card(
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10),
              side: const BorderSide(color: Colors.blue)),
          child: Container(
            width: 200,
            padding: const EdgeInsets.all(10.0),
            child: Text(text, style: const TextStyle(fontSize: 20)),
          ),
        ),
      ],
    );
  }
}
