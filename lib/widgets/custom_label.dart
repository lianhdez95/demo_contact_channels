import 'package:flutter/material.dart';

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