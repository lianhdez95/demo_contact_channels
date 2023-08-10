// ignore_for_file: avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class GetContactsChannelPage extends StatefulWidget {
  const GetContactsChannelPage({Key? key}) : super(key: key);

  @override
  // ignore: library_private_types_in_public_api
  _GetContactChannelPageState createState() => _GetContactChannelPageState();
}

class _GetContactChannelPageState extends State<GetContactsChannelPage> {
  static const platform = MethodChannel('com.example.contacts');
  List<String> _contactNames = [];
  List<String> _contactPhones = [];

  Future<void> _getContacts() async {
  List<dynamic>? contacts;
  try {
    final dynamic result = await platform.invokeMethod<List<dynamic>>('getContacts');
    contacts = result;
  } on PlatformException catch (e) {
    print("Failed to get contacts: '${e.message}'.");
    return;
  }

  final Set<String> uniqueNames = {};
  final Set<String> uniquePhones = {};

  for (dynamic contact in contacts!) {
    if (contact is Map<dynamic, dynamic>) {
      final String? name = contact['name'];
      final String? phone = contact['phone'];
      if (name != null && phone != null) {
        uniqueNames.add(name);
        uniquePhones.add(phone);
      }
    }
  }

  setState(() {
    _contactNames = uniqueNames.toList();
    _contactPhones = uniquePhones.toList();
  });
}

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Contact Channel'),
      ),
      body: _contactNames.isEmpty
          ? const Center(child: Text('No contacts found'))
          : ListView.separated(
              itemCount: _contactNames.length,
              separatorBuilder: (BuildContext context, int index) =>
                  const Divider(),
              itemBuilder: (BuildContext context, int index) {
                final name = _contactNames[index];
                final phone = _contactPhones[index];

                if (name.isEmpty) {
                  return Container(); // No muestra los contactos sin nombre
                }

                return ListTile(
                  leading: const CircleAvatar(
                    backgroundColor: Colors.transparent,
                    child: Icon(
                      Icons.account_circle,
                      color: Colors.blue,
                      size: 40,
                    ),
                  ),
                  title: Text(name),
                  subtitle: Text(phone),
                  trailing: const Icon(Icons.arrow_forward_ios),
                  onTap: () {
                    // Implementación del onTap
                  },
                );
              },
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: _getContacts,
        tooltip: 'Get Contacts',
        child: const Icon(Icons.contacts),
      ),
    );
  }
}