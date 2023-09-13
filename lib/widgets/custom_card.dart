import 'package:flutter/material.dart';


Widget customCard(String text) {

    return Card(
      shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10),
          side: const BorderSide(color: Colors.blue)),
      child: Container(
        width: 150,
        padding: const EdgeInsets.all(10.0),
        child: Text(text, style: const TextStyle(fontSize: 20)),
      ),
    );
  }