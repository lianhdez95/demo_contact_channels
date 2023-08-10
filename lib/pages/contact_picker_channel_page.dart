// ignore_for_file: avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class ContactPickerChannelPage extends StatefulWidget {
  const ContactPickerChannelPage({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _ContactPickerChannelPageState createState() => _ContactPickerChannelPageState();
}

class _ContactPickerChannelPageState extends State<ContactPickerChannelPage> {
  static const MethodChannel _channel = MethodChannel('com.example.contact_picker');

  List<String> selectedContacts = [];

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
            ElevatedButton(
              child: const Text('Open Contact Picker'),
              onPressed: () {
                openContactPicker();
              },
            ),
            const SizedBox(height: 16),
            const Text('Selected Contacts:'),
            Expanded(
              child: ListView.builder(
                itemCount: selectedContacts.length,
                itemBuilder: (context, index) {
                  return ListTile(
                    title: Text(selectedContacts[index]),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  Future<void> openContactPicker() async {
    try {
      List<dynamic> result = await _channel.invokeMethod('openContactPicker');
      // ignore: unnecessary_null_comparison
      if (result != null) {
        setState(() {
          selectedContacts = List<String>.from(result);
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
}