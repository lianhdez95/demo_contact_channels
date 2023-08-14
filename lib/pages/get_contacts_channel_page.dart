// ignore_for_file: avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class GetContactsChannelPage extends StatefulWidget {
  const GetContactsChannelPage({super.key});

  @override
  State<GetContactsChannelPage> createState() => _GetContactChannelPageState();
}

class _GetContactChannelPageState extends State<GetContactsChannelPage> {
  static const platform = MethodChannel('com.example.contacts');
  List<String> _contactNames = [];
  List<String> _contactPhones = [];

  Future<void> _getContacts() async {
    List<String> contacts;
    try {
      final dynamic result = await platform.invokeMethod('getContacts');
      contacts = result.cast<String>();
    } on PlatformException catch (e) {
      print("Failed to get contacts: '${e.message}'.");
      return;
    }

    final Set<String> uniqueNames = {};
    final Set<String> uniquePhones = {};

    for (String contact in contacts) {
      final parts = contact.split(' - ');
      if (parts.length >= 2) {
        uniqueNames.add(parts[0]);
        uniquePhones.add(parts[1]);
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
            trailing: const Icon(
                Icons.arrow_forward_ios
            ),
            onTap: (){
              //implementación del OnTap
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